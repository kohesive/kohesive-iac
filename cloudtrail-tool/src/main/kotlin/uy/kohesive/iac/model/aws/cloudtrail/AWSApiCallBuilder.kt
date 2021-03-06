package uy.kohesive.iac.model.aws.cloudtrail

import com.amazonaws.codegen.emitters.CodeEmitter
import com.amazonaws.codegen.emitters.FreemarkerGeneratorTask
import com.amazonaws.codegen.emitters.GeneratorTaskExecutor
import com.amazonaws.codegen.model.intermediate.*
import freemarker.template.Template
import uy.kohesive.iac.model.aws.cloudtrail.postprocessing.RequestPostProcessors
import uy.kohesive.iac.model.aws.codegen.TemplateDescriptor
import java.io.StringWriter
import java.io.Writer

data class AWSApiCallData(
    val packageName: String,
    val className: String,
    val imports: List<String>,
    val requestNode: RequestMapNode,
    val operation: OperationModel,
    val awsModel: IntermediateModel,
    val event: CloudTrailEvent
)

class AWSApiCallBuilder(
    val intermediateModel: IntermediateModel,
    val event: CloudTrailEvent,
    val className: String,
    val packageName: String
) {

    companion object {
        val XSITypeValue = "xsi:type"
    }

    private fun createMap(requestMap: RequestMap, mapModel: MapModel): RequestMapNode {
        val members = requestMap.mapValues {
            if (mapModel.isValueSimple) {
                RequestMapNode.simple(
                    type  = mapModel.valueType,
                    value = it.value
                )
            } else {
                val memberShape = mapModel.valueModel.shape ?: intermediateModel.shapes[mapModel.valueModel.c2jShape]
                    ?: throw RuntimeException("Can't locate shape for ${mapModel.valueModel.c2jShape}")

                (it.value as? RequestMap)?.let { subRequestMap ->
                    createRequestMapNode(subRequestMap, memberShape, parentRequestObject = requestMap)
                } ?: throw RuntimeException("Complex value of map is not of java.util.Map type")
            }
        }.map {
            RequestMapNodeMember(
                key   = it.key,
                value = it.value,
                memberModel = mapModel.valueModel
            )
        }

        return RequestMapNode.map(mapModel, members)
    }

    private fun createEnumNode(value: String, shape: ShapeModel): RequestMapNode {
        val enumClass      = Class.forName(intermediateModel.metadata.packageName + ".model." + shape.c2jName)
        val nameMethod     = enumClass.methods.first { it.name == "name" }
        val toStringToName = (enumClass.methods.firstOrNull { it.name == "values" }?.invoke(null) as Array<*>).map {
            it.toString() to nameMethod.invoke(it) as String
        }.toMap()

        // Let's find the actual value
        return RequestMapNode.enum(toStringToName[value] ?: throw IllegalStateException("Can't figure out enum value of ${shape.c2jName} by '$value'"), shape)
    }

    private fun createListModelFromMap(requestMap: RequestMap, listModel: ListModel): RequestMapNode =
        createListModelFromList(requestMap.keys.firstOrNull()?.let { itemsKey ->
            requestMap[itemsKey] as? List<Any?>
        }.orEmpty(), listModel)

    private fun createListModelFromList(list: List<Any?>, listModel: ListModel): RequestMapNode =
        if (listModel.isSimple) {
            val simpleValues = list.filterNotNull().flatMap { listItem ->
                (listItem as? Map<*, *>)?.values?.filterNotNull() ?: listOf(listItem)
            }

            RequestMapNode.list(listModel, simpleValues.map { simpleValue ->
                RequestMapNodeMember(listModel.listMemberModel, RequestMapNode.simple(
                    type  = listModel.memberType,
                    value = simpleValue
                ))
            })
        } else {
            val memberShape = listModel.listMemberModel.shape ?: intermediateModel.shapes[listModel.listMemberModel.c2jShape]
                ?: throw RuntimeException("Can't locate shape for ${listModel.listMemberModel.c2jName}")

            RequestMapNode.list(listModel, list.map { listEntry ->
                (listEntry as? RequestMap)?.let { itemAsMap ->
                    createRequestMapNode(itemAsMap, memberShape, parentRequestObject = list)
                }
            }.filterNotNull().map { requestMapNode ->
                RequestMapNodeMember(
                    memberModel = listModel.listMemberModel,
                    value       = requestMapNode
                )
            })
        }

    private fun createRequestMapNode(requestMap: RequestMap, shapeModel: ShapeModel, parentRequestObject: Any?): RequestMapNode {
        // Override shape for abstract class values containing xsi:type fields
        val actualShapeModel = if (requestMap.containsKey(XSITypeValue)) {
            val xsiType = requestMap[XSITypeValue]
            intermediateModel.shapes[xsiType] ?: throw IllegalStateException("Can't locate shape $xsiType")
        } else {
            shapeModel
        }

        // Collect the possible member names map
        var membersAsMap = actualShapeModel.membersAsMap.mapKeys { it.key.toLowerCase() }.orEmpty() + actualShapeModel.members?.associate {
            (it.http?.unmarshallLocationName?.toLowerCase() ?: "\$NONE") to it
        }.orEmpty()
        membersAsMap += membersAsMap.filter { it.value.isList && !it.key.endsWith("Set") }.mapKeys {
            it.key + "set"
        }
        membersAsMap += membersAsMap.filter { it.value.isList && !it.key.endsWith("Set") && it.key.endsWith("s") }.mapKeys {
            it.key.dropLast(1) + "set"
        }

        // Map members to AWS model-aware request nodes
        val nodeMembers = requestMap.filterKeys {
            it != XSITypeValue && !it.startsWith("xmlns")
        }.filterValues { it != null }.map {
            val fieldName  = it.key
            val fieldValue = it.value

            val memberModel = membersAsMap[fieldName.toLowerCase()]
            if (memberModel == null) {
                // Could be an enum
                val enumModel = actualShapeModel.findEnumModelByValue(fieldValue?.toString())
                if (enumModel != null) {
                    return RequestMapNode.enum(enumModel.name, actualShapeModel)
                }

                throw RuntimeException("Shape ${actualShapeModel.shapeName} doesn't have member $fieldName")
            }

            val fieldValueNode = if (memberModel.isSimple) {
                if (memberModel.c2jShape.startsWith("Timestamp")) {
                    (fieldValue as? String)?.let { dateStr ->
                        RequestMapNode.date(dateStr)
                    } ?: throw IllegalStateException("Invalid date value for Timestamp member: $fieldValue")
                } else {
                    RequestMapNode.simple(memberModel.c2jShape, fieldValue)
                }
            } else {
                if (memberModel.isList) {
                    if (fieldValue is Map<*, *>) {
                        createListModelFromMap(fieldValue as Map<String, Any>, memberModel.listModel)
                    } else if (fieldValue is List<*>) {
                        createListModelFromList(fieldValue as List<Any>, memberModel.listModel)
                    } else {
                        throw RuntimeException("List member $fieldName of ${actualShapeModel.c2jName} is of unsupported type ${fieldValue?.javaClass?.simpleName}")
                    }
                } else if (memberModel.isMap) {
                    (fieldValue as? RequestMap)?.let { subRequestMap ->
                        createMap(subRequestMap, memberModel.mapModel)
                    } ?: throw IllegalStateException(
                            "$fieldName of ${actualShapeModel.c2jName} is ${fieldValue?.javaClass?.simpleName} while expected to be Map<String, Any>"
                    )
                } else if (memberModel.enumType != null) {
                    val memberShapeModel = memberModel.shape ?: intermediateModel.shapes[memberModel.c2jShape]
                        ?: throw RuntimeException("Can't locate shape for ${memberModel.c2jName} member of ${actualShapeModel.c2jName}")

                    val enumValue = when (fieldValue) {
                        is String  -> fieldValue
                        is List<*> -> fieldValue.firstOrNull()?.toString()
                        null -> throw RuntimeException("Enum type member has empty value")
                        else -> throw RuntimeException("Unsupported Enum type holder type: ${fieldValue.javaClass.simpleName}")
                    }

                    createEnumNode(enumValue ?: throw RuntimeException("Enum type member has empty value"), memberShapeModel)
                } else {
                    // Structure
                    val memberShapeModel = memberModel.shape ?: intermediateModel.shapes[memberModel.c2jShape]
                        ?: throw RuntimeException("Can't locate shape for ${memberModel.c2jName} member of ${actualShapeModel.c2jName}")

                    (fieldValue as? RequestMap)?.let { subRequestMap ->
                        createRequestMapNode(subRequestMap, memberShapeModel, parentRequestObject = requestMap)
                    } ?: throw IllegalStateException(
                        "$fieldName of ${actualShapeModel.c2jName} is ${fieldValue?.javaClass?.simpleName} while expected to be Map<String, Any>"
                    )
                }
            }

            RequestMapNodeMember(
                memberModel = memberModel,
                value       = fieldValueNode
            )
        }.filterNot { it.value?.isEmpty() ?: true }

        return RequestMapNode.complex(actualShapeModel, nodeMembers)
    }

    private fun RequestMapNode.collectShapeNames(): Set<String> {
        return HashSet<String>().apply {
            this@collectShapeNames.collectShapeNames(this)
        }
    }
    private fun RequestMapNode.collectShapeNames(shapesSet: MutableSet<String>) {
        if (isStructure()) {
            this.shape?.let { shape ->
                shapesSet.add(shape.c2jName)
            }
        }
        (members + constructorArgs).forEach { it.value?.collectShapeNames(shapesSet) }
    }

    fun build(template: TemplateDescriptor): String {
        val generatorTaskExecutor = GeneratorTaskExecutor()

        val requestShape = intermediateModel.shapes[event.eventName + "Request"] ?: throw IllegalStateException("Can't find a shape for event $event")
        val operation    = intermediateModel.getOperation(event.eventName) ?: throw IllegalStateException("Can't find an operation for event $event")

        val requestNode  = RequestPostProcessors.postProcess(
            requestMapNode = createRequestMapNode(event.request.orEmpty(), requestShape, parentRequestObject = null),
            awsModel       = intermediateModel
        )

        val imports = requestNode.collectShapeNames().filterNot { it.endsWith("Input") || it.startsWith(operation.operationName) }.map { shapeName ->
            "${intermediateModel.metadata.packageName}.model.$shapeName"
        } + listOf(
            "${intermediateModel.metadata.packageName}.model.*",
            "${intermediateModel.metadata.packageName}.${intermediateModel.metadata.syncInterface}",
            "java.util.*"
        )

        val apiCallData  = AWSApiCallData(
            requestNode  = requestNode,
            awsModel     = intermediateModel,
            event        = event,
            operation    = operation,
            packageName  = packageName,
            className    = className,
            imports      = imports
        )

        val stringWriter = StringWriter()
        val codeEmitter  = CodeEmitter(listOf(
            GenerateApiCallsTask.create(stringWriter, apiCallData, template)
        ), generatorTaskExecutor)
        codeEmitter.emit()

        generatorTaskExecutor.waitForCompletion()
        generatorTaskExecutor.shutdown()

        return stringWriter.buffer.toString()
    }

}

class GenerateApiCallsTask private constructor(writer: Writer, template: Template, data: Any)
    : FreemarkerGeneratorTask(writer, template, data) {

    companion object {
        fun create(stringWriter: StringWriter, data: AWSApiCallData, template: TemplateDescriptor): GenerateApiCallsTask {
            return GenerateApiCallsTask(
                stringWriter,
                template.load(),
                data
            )
        }
    }

}