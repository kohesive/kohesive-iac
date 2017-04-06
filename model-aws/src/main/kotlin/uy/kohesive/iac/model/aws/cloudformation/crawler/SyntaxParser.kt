package uy.kohesive.iac.model.aws.cloudformation.crawler

import uy.klutter.core.common.mustNotEndWith
import uy.klutter.core.common.mustNotStartWith

object CloudFormationExampleSyntaxParser {

    val PropertiesSectionRegexp = "\"Properties\"\\s*:\\s*\\{(.*)\\}\\s+\\}".toRegex(RegexOption.DOT_MATCHES_ALL)

    private fun getPropertiesBlock(syntaxBadJson: String) = PropertiesSectionRegexp.find(syntaxBadJson)?.groups?.get(1)?.value

    fun parse(syntaxBadJson: String, rootResource: Boolean): Map<String, String>? {
        try {
            return (if (rootResource) {
                getPropertiesBlock(syntaxBadJson)
            } else {
                syntaxBadJson.split('\n').drop(1).dropLast(1).joinToString("\n")
            })?.split(",\n")?.map(String::trim)?.map { entry ->
                entry.split("\\s+:\\s+".toRegex()).let { split ->
                    split[0].mustNotStartWith('"').mustNotEndWith('"') to split[1]
                }
            }?.toMap().orEmpty()
        } catch (t: Throwable) {
            return null
        }
    }


}