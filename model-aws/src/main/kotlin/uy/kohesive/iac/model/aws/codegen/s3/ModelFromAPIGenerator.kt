package uy.kohesive.iac.model.aws.codegen.s3

import com.amazonaws.codegen.C2jModels
import com.amazonaws.codegen.CodeGenerator
import com.amazonaws.codegen.model.config.BasicCodeGenConfig
import com.amazonaws.codegen.model.config.customization.CustomizationConfig
import com.amazonaws.codegen.model.service.*
import com.amazonaws.http.HttpMethodName
import org.reflections.Reflections
import org.reflections.scanners.TypeElementsScanner
import uy.klutter.core.common.mustNotStartWith
import uy.kohesive.iac.model.aws.utils.firstLetterToUpperCase
import uy.kohesive.iac.model.aws.utils.namespace
import uy.kohesive.iac.model.aws.utils.simpleName
import java.util.*

class ModelFromAPIGenerator(
    val serviceInterface: Class<*>,
    val serviceMetadata: ServiceMetadata,
    val outputDir: String,
    val verbToHttpMethod: Map<String, HttpMethodName>,
    val fileNamePrefix: String,
    serviceSourcesDir: String
) {

    private val interfaceSimpleName: String = serviceInterface.simpleName
    private val packageName: String = serviceInterface.canonicalName.namespace()
    private val modelPackage = packageName + ".model"

    private val modelClassFqNamesBySimpleName = TypeElementsScanner().apply {
        Reflections(modelPackage, this)
    }.store.keySet().associateBy(String::simpleName) + mapOf(
        "HttpMethod" to "com.amazonaws.HttpMethod"
    )

    private val sources = SourceCache(serviceSourcesDir)

    private val codeGenConfig = BasicCodeGenConfig(
        interfaceSimpleName,
        packageName,
        null,
        null,
        null
    )

    private val classFqNameToShape = HashMap<String, Pair<String, Shape>>()

    private fun getModelClassBySimpleName(simpleName: String) = modelClassFqNamesBySimpleName[simpleName]?.let { fqName ->
        Class.forName(fqName)
    } ?: throw IllegalArgumentException("Unknown model class: $simpleName")

    private fun getOrCreateShape(clazz: Class<*>, typeParameterFqName: List<String> = emptyList(), shapeNameOverride: String? = null): Pair<String, Shape> =
        (clazz.name + (if (typeParameterFqName.isEmpty()) "" else "<${typeParameterFqName.joinToString(", ")}>")).let { classFqName ->
            var fillWithMembers = false

            classFqNameToShape.getOrPut(clazz.canonicalName) {
                val nameAndShape = if (clazz.isPrimitive) {
                    clazz.name.firstLetterToUpperCase() to Shape().apply { type = clazz.name.toLowerCase() }
                } else if (clazz.name.startsWith("java.lang.")) {
                    clazz.simpleName to Shape().apply { type = clazz.simpleName.toLowerCase() }
                } else if (clazz.name == "java.util.Date") {
                    "Date" to Shape().apply { type = "timestamp" }
                } else if (clazz.name.endsWith("[]") || clazz.name.namespace().let { it == "java.io" || it == "java.net" }) {
                    throw UnmodelableOperation()
                } else if (List::class.java.isAssignableFrom(clazz)) {
                    if (typeParameterFqName.isEmpty()) throw IllegalStateException("Un-parameterized list")

                    val listParameterFqName = typeParameterFqName.first()
                    listParameterFqName.simpleName() + "List" to Shape().apply {
                        type       = "list"
                        listMember = Member().apply {
                            shape = getOrCreateShape(Class.forName(listParameterFqName)).first
                        }
                    }
                } else if (Map::class.java.isAssignableFrom(clazz)) {
                    if (typeParameterFqName.isEmpty()) throw IllegalStateException("Un-parameterized map")

                    val mapKeyParameterFqName   = typeParameterFqName[0]
                    val mapValueParameterFqName = typeParameterFqName[1]
                    mapKeyParameterFqName.simpleName() + "Map" to Shape().apply {
                        type       = "map"
                        mapKeyType = Member().apply {
                            shape = getOrCreateShape(Class.forName(mapKeyParameterFqName)).first
                        }
                        mapValueType = Member().apply {
                            shape = getOrCreateShape(Class.forName(mapValueParameterFqName)).first
                        }
                    }
                } else {
                    fillWithMembers = true
                    clazz.simpleName to Shape().apply {
                        type = "structure"
                    }
                }

                // Override shape name
                shapeNameOverride?.let { it to nameAndShape.second } ?: nameAndShape
            }.apply {
                // We fill shape members after shape is added to map to avoid infinite loops
                if (fillWithMembers) {
                    val membersByGetters = clazz.declaredMethods.filter { method ->
                        method.name.startsWith("get") || method.name.startsWith("is")
                    }.map { getter ->
                        getter.name.mustNotStartWith("get").mustNotStartWith("is") to getter.returnType
                    }.toMap()

                    // TODO: implement
                }
            }
        }

    private fun createOperations(): List<Operation> {
        val requestMethodsToRequestClasses = serviceInterface.methods.groupBy { it.name }.mapValues {
            it.value.map { method ->
                method to method.parameters.firstOrNull { parameter ->
                    parameter.type.simpleName.endsWith("Request")
                }?.type
            }.firstOrNull()
        }.values.filterNotNull().toMap().filterValues { it != null }.mapValues { it.value!! }.filter {
            "${it.key.name.firstLetterToUpperCase()}Request" == it.value.simpleName
        }

        return requestMethodsToRequestClasses.map {
            try {
                val requestMethod = it.key
                val requestClass  = it.value
                val resultClass   = it.key.returnType

                val operationName = requestMethod.name.firstLetterToUpperCase()

                val httpMethod = verbToHttpMethod.keys.firstOrNull { operationName.startsWith(it) }?.let { verb ->
                    verbToHttpMethod[verb]
                }?.name ?: throw IllegalStateException("Can't figure out HTTP method for $operationName")

                val http = Http()
                    .withMethod(httpMethod)
                    .withRequestUri("/")

                val input = Input().apply {
                    shape = operationName + "Input"
                }
                getOrCreateShape(requestClass, shapeNameOverride = input.shape)

                val output = Output().apply {
                    shape = if (resultClass.simpleName.endsWith("Result")) {
                        operationName + "Output"
                    } else {
                        resultClass.simpleName
                    }
                }
                getOrCreateShape(resultClass, shapeNameOverride = output.shape)

                Operation()
                    .withName(operationName)
                    .withInput(input)
                    .withHttp(http).apply { setOutput(output) }
            } catch (ume: UnmodelableOperation) {
                // ignore this operation
                null
            }
        }.filterNotNull()
    }

    fun generate() {
        val operations = createOperations()

        val c2jModels = C2jModels.builder()
            .codeGenConfig(codeGenConfig)
            .customizationConfig(CustomizationConfig.DEFAULT)
            .serviceModel(ServiceModel(
                serviceMetadata,
                operations.associateBy { it.name },
                classFqNameToShape.values.toMap(),
                HashMap<String, Authorizer>() // TODO: add authorizers
            )
        ).build()

        CodeGenerator(c2jModels, outputDir, outputDir, fileNamePrefix).execute()
    }

}

class UnmodelableOperation : Exception()