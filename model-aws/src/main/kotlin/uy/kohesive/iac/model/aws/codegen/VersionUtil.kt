package uy.kohesive.iac.model.aws.codegen

object VersionUtil {

    private val VersionedPattern = ".*\\s((v|V)\\d+)".toRegex()

    fun getExplicitServiceNameVersion(serviceName: String): String?
        = VersionedPattern.find(serviceName)?.groupValues?.getOrNull(1)

}