package uy.kohesive.iac.model.aws.cloudtrail

import com.amazonaws.codegen.emitters.FreemarkerGeneratorTask
import com.amazonaws.codegen.emitters.GeneratorTaskExecutor
import com.amazonaws.codegen.model.intermediate.IntermediateModel
import com.amazonaws.codegen.model.intermediate.ListModel
import com.amazonaws.codegen.model.intermediate.MapModel
import com.amazonaws.codegen.model.intermediate.ShapeModel
import freemarker.template.Template
import uy.kohesive.iac.model.aws.codegen.TemplateDescriptor
import java.io.StringWriter
import java.io.Writer

class AWSApiCallBuilder(
    val awsModel: IntermediateModel,
    val event: CloudTrailEvent
) {

    private fun createMap(requestMap: RequestMap, mapModel: MapModel): RequestMapNode {
        // TODO: do we need to support complex keys?
        val members = requestMap.mapValues {
            if (mapModel.isValueSimple) {
                RequestMapNode.simple(
                    type  = mapModel.valueType,
                    value = it.value
                )
            } else {
                val memberShape = mapModel.valueModel.shape ?: awsModel.shapes[mapModel.valueModel.c2jShape]
                    ?: throw RuntimeException("Can't locate shape for ${mapModel.valueModel.c2jShape}")

                (it.value as? RequestMap)?.let { subRequestMap ->
                    createRequestMapNode(subRequestMap, memberShape)
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
            val memberShape = listModel.listMemberModel.shape ?: awsModel.shapes[listModel.listMemberModel.c2jShape]
                ?: throw RuntimeException("Can't locate shape for ${listModel.listMemberModel.c2jName}")

            RequestMapNode.list(listModel, list.map { listEntry ->
                (listEntry as? Map<String, Any?>)?.let { itemAsMap ->
                    createRequestMapNode(itemAsMap, memberShape)
                }
            }.filterNotNull().map { requestMapNode ->
                RequestMapNodeMember(
                    memberModel = listModel.listMemberModel,
                    value       = requestMapNode
                )
            })
        }

    private fun createRequestMapNode(requestMap: RequestMap, shapeModel: ShapeModel): RequestMapNode {
        var membersAsMap = shapeModel.membersAsMap.mapKeys { it.key.toLowerCase() }.orEmpty() + shapeModel.members?.associate {
            (it.http?.unmarshallLocationName?.toLowerCase() ?: "\$NONE") to it
        }.orEmpty()
        membersAsMap += membersAsMap.filter { it.value.isList && !it.key.endsWith("Set") }.mapKeys {
            it.key + "set"
        }
        membersAsMap += membersAsMap.filter { it.value.isList && !it.key.endsWith("Set") && it.key.endsWith("s") }.mapKeys {
            it.key.dropLast(1) + "set"
        }

        val nodeMembers = requestMap.filterValues { it != null }.map {
            val fieldName  = it.key
            val fieldValue = it.value

            val memberModel = membersAsMap[fieldName.toLowerCase()] ?: throw RuntimeException("Shape ${shapeModel.shapeName} doesn't have member $fieldName")

            val fieldValueNode = if (memberModel.isSimple) {
                RequestMapNode.simple(memberModel.c2jShape, fieldValue)
            } else {
                if (memberModel.isList) {
                    if (fieldValue is Map<*, *>) {
                        createListModelFromMap(fieldValue as Map<String, Any>, memberModel.listModel)
                    } else if (fieldValue is List<*>) {
                        createListModelFromList(fieldValue as List<Any>, memberModel.listModel)
                    } else {
                        throw RuntimeException("List member $fieldName of ${shapeModel.c2jName} is of unsupported type ${fieldValue?.javaClass?.simpleName}")
                    }
                } else if (memberModel.isMap) {
                    (fieldValue as? RequestMap)?.let { subRequestMap ->
                        createMap(subRequestMap, memberModel.mapModel)
                    } ?: throw IllegalStateException(
                        "$fieldName of ${shapeModel.c2jName} is ${fieldValue?.javaClass?.simpleName} while expected to be Map<String, Any>"
                    )
                } else {
                    val memberShapeModel = memberModel.shape ?: awsModel.shapes[memberModel.c2jShape]
                    if (memberShapeModel == null) {
                        throw RuntimeException("Can't locate shape for ${memberModel.c2jName} member of ${shapeModel.c2jName}")
                    }

                    (fieldValue as? RequestMap)?.let { subRequestMap ->
                        createRequestMapNode(subRequestMap, memberShapeModel)
                    } ?: throw IllegalStateException(
                        "$fieldName of ${shapeModel.c2jName} is ${fieldValue?.javaClass?.simpleName} while expected to be Map<String, Any>"
                    )
                }
            }

            RequestMapNodeMember(
                memberModel = memberModel,
                value       = fieldValueNode
            )
        }

        return RequestMapNode.complex(shapeModel, nodeMembers)
    }

    fun build(): String {
        val generatorTaskExecutor = GeneratorTaskExecutor()

        val apiCallData = ApiCallData(
            shape      = awsModel.shapes[event.eventName + "Request"] ?: throw IllegalStateException("Can't find a shape for event $event"),
            requestMap = event.request.orEmpty()
        )

        val requestNode = createRequestMapNode(apiCallData.requestMap, apiCallData.shape)

        val stringWriter = StringWriter()
//        val emitter      = CodeEmitter(listOf(GenerateApiCallsTask.create(stringWriter, apiCallData)), generatorTaskExecutor)
//        emitter.emit()
//
//        generatorTaskExecutor.waitForCompletion()
//        generatorTaskExecutor.shutdown()

        return stringWriter.buffer.toString()
    }

}

class GenerateApiCallsTask private constructor(writer: Writer, template: Template, data: Any)
    : FreemarkerGeneratorTask(writer, template, data) {

    companion object {
        fun create(stringWriter: StringWriter, data: ApiCallData): GenerateApiCallsTask {
            return GenerateApiCallsTask(
                stringWriter,
                TemplateDescriptor.RequestBuilder.load(),
                data
            )
        }
    }

}