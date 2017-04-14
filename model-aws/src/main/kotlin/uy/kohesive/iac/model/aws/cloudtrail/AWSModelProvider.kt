package uy.kohesive.iac.model.aws.cloudtrail

import com.amazonaws.codegen.model.intermediate.IntermediateModel
import com.amazonaws.codegen.utils.ModelLoaderUtils
import org.reflections.Reflections
import org.reflections.scanners.ResourcesScanner
import uy.kohesive.iac.model.aws.codegen.loadModel
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class AWSModelProvider {

    companion object {
        val IntermediateFilenameRegexp = "(.*)-(\\d{4}-\\d{2}-\\d{2})-intermediate\\.json".toRegex()
    }

    private val intermediateFiles: Set<String>
        = Reflections("models", ResourcesScanner()).getResources(IntermediateFilenameRegexp.toPattern())

    // Service name -> version -> file
    private val serviceShortNameToVersions: Map<String, SortedMap<String, String>> by lazy {
        intermediateFiles.map { filePath ->
            IntermediateFilenameRegexp.find(filePath.takeLastWhile { it != '/' })?.groupValues?.let {
                val serviceName = it[1]
                val apiVersion  = it[2].replace("-", "")

                serviceName to (apiVersion to filePath)
            }
        }.filterNotNull().groupBy { it.first }.mapValues {
            it.value.map { it.second }.toMap().toSortedMap()
        }
    }

    private val modelCache = ConcurrentHashMap<String, IntermediateModel>()

    fun getModel(service: String, apiVersion: String? = null): IntermediateModel {
        return serviceShortNameToVersions[service]?.let { versionToFilepath ->
            val filePath = if (apiVersion == null) {
                versionToFilepath.values.first()
            } else {
                versionToFilepath[apiVersion.replace("_", "")] ?: {
                    versionToFilepath.values.first()
                }()
            }

            modelCache.getOrPut(filePath) {
                ModelLoaderUtils.getRequiredResourceAsStream(filePath).use { stream ->
                    loadModel<IntermediateModel>(stream)
                }
            }
        } ?: throw IllegalArgumentException("Unknown service: $service")
    }

}