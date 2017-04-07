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
import uy.kohesive.iac.model.aws.utils.namespace

class ModelFromAPIGenerator(
    serviceInterface: Class<*>,
    val serviceMetadata: ServiceMetadata,
    val outputDir: String,
    val verbToHttpMethod: Map<String, HttpMethodName>,
    val fileNamePrefix: String,
    serviceSourcesDir: String
) {

    private val interfaceSimpleName: String = serviceInterface.simpleName
    private val packageName: String = serviceInterface.canonicalName.namespace()
    private val modelPackage = packageName + ".model"

    private val typeElementsScanner = TypeElementsScanner().apply {
        Reflections(modelPackage, this)
    }

    private val sources = SourceCache(serviceSourcesDir)

    private val codeGenConfig = BasicCodeGenConfig(
        interfaceSimpleName,
        packageName,
        null,
        null,
        null
    )

    private val shapes = HashMap<String, Shape>().apply {
        listOf(
            java.lang.String::class.java,
            java.lang.Integer::class.java,
            java.lang.Long::class.java,
            java.lang.Boolean::class.java
        ).forEach { autoboxType ->
            put(autoboxType.simpleName, Shape().apply {
                type = autoboxType.simpleName.toLowerCase()
            })
        }
    }

    private fun createShape(shapeName: String, classFqName: String) = Shape().apply {
        val clazz = try { Class.forName(classFqName) } catch (cnf: ClassNotFoundException) { Void::class.java }

        val membersByGetters = clazz.declaredMethods.filter { method ->
            method.name.startsWith("get") || method.name.startsWith("is")
        }.map { getter ->
            getter.name.mustNotStartWith("get").mustNotStartWith("is") to getter.returnType
        }.toMap()

        members = membersByGetters.mapValues {
            val memberShape = if (it.value.isPrimitive) {
                it.value.name
            } else if (it.value.name.startsWith("java.lang.")) {
                it.value.simpleName
            } else if (List::class.java.isAssignableFrom(it.value)) {
                it.key + "List" // TODO: save the shape
            } else if (Set::class.java.isAssignableFrom(it.value)) {
                it.key + "Set" // TODO: save the shape
            } else if (Map::class.java.isAssignableFrom(it.value)) {
                it.key + "Map" // TODO: save the shape
            } else {
                // TODO: save the shape
                it.value.simpleName
            }

            Member().apply {
                shape = memberShape
            }
        }

        type = "structure"

        shapes[shapeName] = this@apply
    }

    private fun createOperations(): List<Operation> {
        // TODO: implement
        return emptyList()
    }

    fun generate() {
        val c2jModels = C2jModels.builder()
            .codeGenConfig(codeGenConfig)
            .customizationConfig(CustomizationConfig.DEFAULT)
            .serviceModel(ServiceModel(
                serviceMetadata,
                createOperations().associateBy { it.name },
                shapes,
                HashMap<String, Authorizer>() // TODO: add authorizers
            )
        ).build()

        CodeGenerator(c2jModels, outputDir, outputDir, fileNamePrefix).execute()
    }

}