package uy.kohesive.iac.model.aws.codegen.model

import com.amazonaws.codegen.C2jModels
import com.amazonaws.codegen.CodeGenerator
import com.amazonaws.codegen.model.config.BasicCodeGenConfig
import com.amazonaws.codegen.model.config.customization.CustomizationConfig
import com.amazonaws.codegen.model.service.*
import com.amazonaws.http.HttpMethodName
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.type.ClassOrInterfaceType
import org.reflections.Reflections
import org.reflections.scanners.TypeElementsScanner
import uy.klutter.core.common.mustNotStartWith
import uy.kohesive.iac.model.aws.utils.firstLetterToUpperCase
import uy.kohesive.iac.model.aws.utils.namespace
import uy.kohesive.iac.model.aws.utils.simpleName
import java.util.*

data class EnumHandler(
    val enumClass: Class<*>,
    val keys: List<String>
)
class ModelFromAPIGenerator(
    val serviceInterface: Class<*>,
    val serviceMetadata: ServiceMetadata,
    val outputDir: String,
    val verbToHttpMethod: Map<String, HttpMethodName>,
    val fileNamePrefix: String,
    val enumHandlers: List<EnumHandler>,
    serviceSourcesDir: String
) {

    private val interfaceSimpleName: String = serviceInterface.simpleName
    private val packageName: String = serviceInterface.canonicalName.namespace()
    private val modelPackage = packageName + ".model"

    private val modelClassFqNamesBySimpleName = TypeElementsScanner().apply {
        Reflections(modelPackage, this)
    }.store.keySet().associateBy(String::simpleName).mapKeys {
        if (it.key.contains("$")) {
            it.key.substring(it.key.lastIndexOf('$') + 1)
        } else {
            it.key
        }
    } + listOf(
        java.lang.String::class.java,
        java.lang.Boolean::class.java,
        java.lang.Integer::class.java,
        java.lang.Long::class.java,
        java.lang.Object::class.java
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

    private fun getOrCreateShape(
        clazz: Class<*>,
        typeParameterFqNames: List<Class<*>> = emptyList(),
        shapeNameOverride: String? = null
    ): Pair<String, Shape> =
        (clazz.name + (if (typeParameterFqNames.isEmpty()) "" else "<${typeParameterFqNames.joinToString(", ")}>")).let { classFqName ->
            var fillWithMembers = false

            classFqNameToShape.getOrPut(classFqName) {
                val nameAndShape = if (clazz.isPrimitive) {
                    clazz.name.firstLetterToUpperCase() to Shape().apply { type = clazz.name.toLowerCase() }
                } else if (clazz.isEnum) {
                    val enumHandler = enumHandlers.firstOrNull { it.enumClass == clazz}
                        ?: throw IllegalArgumentException("No enum handler defined for ${clazz.simpleName}")

                    clazz.simpleName to Shape().apply {
                        enumValues = enumHandler.keys.filterNotNull()
                        type = "structure"
                    }
                } else if (clazz.name == "java.lang.Object") {
                    // Looks weird, but seems to be correct. Raw Objects are used in Map values mapped to Strings.
                    "String" to Shape().apply { type = "string" }
                } else if (clazz.name.startsWith("java.lang.")) {
                    clazz.simpleName to Shape().apply { type = clazz.simpleName.toLowerCase() }
                } else if (clazz.name == "java.util.Date") {
                    "Date" to Shape().apply { type = "timestamp" }
                } else if (clazz.name.endsWith("[]") || clazz.name.namespace().let { it == "java.io" || it == "java.net" }) {
                    throw UnModelableOperation()
                } else if (List::class.java.isAssignableFrom(clazz) || Set::class.java.isAssignableFrom(clazz)) {
                    if (typeParameterFqNames.isEmpty()) {
                        throw IllegalStateException("Un-parameterized list")
                    }

                    val listParameter = typeParameterFqNames.first()
                    (listParameter.simpleName + if (Set::class.java.isAssignableFrom(clazz)) "Set" else "List") to Shape().apply {
                        type       = "list"
                        listMember = Member().apply {
                            shape = getOrCreateShape(listParameter).first
                        }
                    }
                } else if (Map::class.java.isAssignableFrom(clazz)) {
                    if (typeParameterFqNames.isEmpty()) {
                        throw UnModelableOperation("Un-parameterized map")
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

                        val typeArguments = getCollectionTypeArguments(clazz, "get$member", memberClass)

                        Member().apply {
                            shape = getOrCreateShape(memberClass, typeParameterFqNames = typeArguments).first
                        }
                    }
                }
            }
        }

    private fun getCollectionTypeArguments(clazz: Class<*>, memberName: String, memberClass: Class<*>): List<Class<*>> =
        if (memberClass.simpleName.let { it == "List" || it == "Set" || it == "Map" }) {
            val classifierFromSources = sources.get(clazz.name).let { compilationUnit ->
                if (clazz.name.contains('$')) {
                    val pathArray = clazz.name.simpleName().split('$')

                    val initialType = compilationUnit.types.filterIsInstance<ClassOrInterfaceDeclaration>().first {
                        it.nameAsString == pathArray[0]
                    }

                    pathArray.drop(1).fold(initialType) { classifier, pathElement ->
                        classifier.childNodes.filterIsInstance<ClassOrInterfaceDeclaration>().first {
                            it.nameAsString == pathElement
                        }
                    }
                } else {
                    if (clazz.isInterface) {
                        compilationUnit.getInterfaceByName(clazz.simpleName).get()
                    } else {
                        compilationUnit.getClassByName(clazz.simpleName).get()
                    }
                }
            }

            classifierFromSources.getMethodsByName(memberName)?.firstOrNull()?.let { getter ->
                (getter.type as? ClassOrInterfaceType)?.typeArguments?.get()?.map { typeArgument ->
                    (typeArgument as? ClassOrInterfaceType)?.nameAsString?.let { simpleName ->
                        getModelClassBySimpleName(simpleName)
                    }
                }
            }.orEmpty().filterNotNull()
        } else {
            emptyList()
        }

    private fun createOperations(): List<Operation> {
        val requestMethodsToRequestClasses = serviceInterface.methods.groupBy { it.name }.mapValues {
            // Here we take the method -> request class pair where request class is not null
            it.value.map { method ->
                method to method.parameters.firstOrNull { parameter ->
                    parameter.type.simpleName.endsWith("Request")
                }?.type
            }.firstOrNull {
                it.second != null // it.second is request class
            }
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
                val typeArguments = getCollectionTypeArguments(serviceInterface, requestMethod.name, resultClass)
                getOrCreateShape(resultClass, shapeNameOverride = output.shape, typeParameterFqNames = typeArguments)

                Operation()
                    .withName(operationName)
                    .withInput(input)
                    .withHttp(http).apply { setOutput(output) }
            } catch (umo: UnModelableOperation) {
                // ignore this operation
                null
            }
        }.filterNotNull()
    }

    fun generate() {
        // Create operations and shapes used in operations
        val operations    = createOperations().sortedBy { it.name }.associateBy { it.name }
        val shapesFromOps = classFqNameToShape.values.toMap()

        // Create shapes for the rest of model classes
        val modelsClassesWithNoShape = (modelClassFqNamesBySimpleName - (modelClassFqNamesBySimpleName.keys.intersect(shapesFromOps.keys))).filter {
            val simpleName = it.key
            val fqName     = it.value

            fqName.namespace() == modelPackage &&
            !simpleName.endsWith("marshaller", ignoreCase = true) &&
            !simpleName.endsWith("Request") &&
            !simpleName.startsWith("Abstract") &&
            simpleName.first().isJavaIdentifierStart() &&
            simpleName.all(Char::isJavaIdentifierPart)
        }
        val shapeFromModelsNames = modelsClassesWithNoShape.map {
            try {
                getOrCreateShape(getModelClassBySimpleName(it.key)).first
            } catch (umo: UnModelableOperation) {
                null
            }
        }.filterNotNull()

        // Create a fake operation to reference the model shapes not used in real operations
        val preserveModelsShape = "KohesiveModelPreserveInput" to Shape().apply {
            type    = "structure"
            members = shapeFromModelsNames.associate { shapeName ->
                shapeName to Member().apply {
                    shape = shapeName
                }
            }
        }
        val preserveModelsOp = "KohesivePreserveShapesOperation" to Operation()
            .withName("KohesivePreserveShapesOperation")
            .withInput(Input().apply {
                shape = "KohesiveModelPreserveInput"
            })
            .withHttp(Http()
                .withMethod("GET")
                .withRequestUri("/")
            )

        // Build the models
        val c2jModels = C2jModels.builder()
            .codeGenConfig(codeGenConfig)
            .customizationConfig(CustomizationConfig.DEFAULT)
            .serviceModel(ServiceModel(
                serviceMetadata,
                operations + preserveModelsOp,
                (classFqNameToShape.values.toMap() + preserveModelsShape).toSortedMap(),
                emptyMap()
            )
        ).build()

        // Generate models JSON and service API
        CodeGenerator(c2jModels, outputDir, outputDir, fileNamePrefix).execute()
    }

}

class UnModelableOperation(override val message: String? = null) : Exception()