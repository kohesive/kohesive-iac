package uy.kohesive.iac.model.aws.codegen.s3

import com.amazonaws.codegen.C2jModels
import com.amazonaws.codegen.CodeGenerator
import com.amazonaws.codegen.model.config.BasicCodeGenConfig
import com.amazonaws.codegen.model.config.customization.CustomizationConfig
import com.amazonaws.codegen.model.service.*
import com.amazonaws.http.HttpMethodName
import com.github.javaparser.ast.type.ClassOrInterfaceType
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
    }.store.keySet().associateBy(String::simpleName) + listOf(
        java.lang.String::class.java,
        java.lang.Boolean::class.java,
        java.lang.Integer::class.java,
        java.lang.Long::class.java
    ).associate { it.simpleName to it.name }

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

    private fun getOrCreateShape(clazz: Class<*>, typeParameterFqNames: List<Class<*>> = emptyList(), shapeNameOverride: String? = null): Pair<String, Shape> =
        (clazz.name + (if (typeParameterFqNames.isEmpty()) "" else "<${typeParameterFqNames.joinToString(", ")}>")).let { classFqName ->
            var fillWithMembers = false

            classFqNameToShape.getOrPut(classFqName) {
                val nameAndShape = if (clazz.isPrimitive) {
                    clazz.name.firstLetterToUpperCase() to Shape().apply { type = clazz.name.toLowerCase() }
                } else if (clazz.name.startsWith("java.lang.")) {
                    clazz.simpleName to Shape().apply { type = clazz.simpleName.toLowerCase() }
                } else if (clazz.name == "java.util.Date") {
                    "Date" to Shape().apply { type = "timestamp" }
                } else if (clazz.name.endsWith("[]") || clazz.name.namespace().let { it == "java.io" || it == "java.net" }) {
                    throw UnmodelableOperation()
                } else if (List::class.java.isAssignableFrom(clazz)) {
                    if (typeParameterFqNames.isEmpty()) {
                        throw IllegalStateException("Un-parameterized list")
                    }

                    val listParameter = typeParameterFqNames.first()
                    listParameter.simpleName + "List" to Shape().apply {
                        type       = "list"
                        listMember = Member().apply {
                            shape = getOrCreateShape(listParameter).first
                        }
                    }
                } else if (Map::class.java.isAssignableFrom(clazz)) {
                    if (typeParameterFqNames.isEmpty()) {
                        throw IllegalStateException("Un-parameterized map")
                    }

                    val mapKeyParameter   = typeParameterFqNames[0]
                    val mapValueParameter = typeParameterFqNames[1]
                    mapValueParameter.simpleName + "Map" to Shape().apply {
                        type       = "map"
                        mapKeyType = Member().apply {
                            shape = getOrCreateShape(mapKeyParameter).first
                        }
                        mapValueType = Member().apply {
                            shape = getOrCreateShape(mapValueParameter).first
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

                    second.members = membersByGetters.mapValues {
                        val member      = it.key
                        val memberClass = it.value

                        val typeArguments = if (memberClass.simpleName.let { it == "List" || it == "Map" }) {
                            val classifierFromSources = sources.get(clazz.name).getClassByName(clazz.simpleName).get()
                            classifierFromSources.getMethodsByName("get$member")?.firstOrNull()?.let { getter ->
                                (getter.type as? ClassOrInterfaceType)?.typeArguments?.get()?.map { typeArgument ->
                                    (typeArgument as? ClassOrInterfaceType)?.nameAsString?.let { simpleName ->
                                        getModelClassBySimpleName(simpleName)
                                    }
                                }
                            }.orEmpty().filterNotNull()
                        } else {
                            emptyList()
                        }

                        Member().apply {
                            shape = getOrCreateShape(memberClass, typeParameterFqNames = typeArguments).first
                        }
                    }
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