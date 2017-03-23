package uy.kohesive.iac.model.aws.cloudformation.crawler

import uy.klutter.core.jdk.mustNotEndWith
import uy.klutter.core.jdk.mustNotStartWith

object CloudFormationExampleSyntaxParser {

    val PropertiesSectionRegexp = "\"Properties\"\\s*:\\s*\\{(.*)\\}\\s+\\}".toRegex(RegexOption.DOT_MATCHES_ALL)

    fun parse(syntaxBadJson: String): Map<String, String> =
        PropertiesSectionRegexp.find(syntaxBadJson)?.groups?.get(1)?.value?.let { propertiesSection ->
            propertiesSection.split(",\n").map(String::trim).map { entry ->
                entry.split("\\s+:\\s+".toRegex()).let { split ->
                    split[0].mustNotStartWith('"').mustNotEndWith('"') to split[1]
                }
            }.toMap()
        }.orEmpty()

}