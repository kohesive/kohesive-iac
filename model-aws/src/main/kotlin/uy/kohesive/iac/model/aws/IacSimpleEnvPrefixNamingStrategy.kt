package uy.kohesive.iac.model.aws

import kotlin.reflect.KClass

class IacSimpleEnvPrefixNamingStrategy : IacContextNamingStrategy {
    override fun nameFor(environment: String, type: String, localName: String): String = "${environment}_${localName}"
}