package uy.kohesive.iac.model.aws

import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import uy.kohesive.iac.model.aws.contexts.*
import uy.kohesive.iac.model.aws.utils.DslScope
import java.util.*

@DslScope
open class IacContext(
        val environment: String,
        val planId: String,
        val namingStrategy: IacContextNamingStrategy = IacSimpleEnvPrefixNamingStrategy(),
        init: IacContext.() -> Unit = {}
) : KohesiveIdentifiable, Ec2Enabled, IamRoleEnabled {
    override val objectsToIds = IdentityHashMap<Any, String>()

    private val variables: MutableMap<String, ParameterizedValue> = hashMapOf()
    private val mappings: MutableMap<String, MappedValues> = hashMapOf()

    override val ec2Client: AmazonEC2 by lazy { DeferredAmazonEC2(this) }
    override val ec2Context: Ec2Context by lazy { Ec2Context(this) }
    override val iamClient: AmazonIdentityManagement by lazy { DeferredAmazonIdentityManagement(this) }
    override val iamContext: IamContext by lazy { IamContext(this) }

    init {
        init()
    }

    fun addVariables(vararg vari: ParameterizedValue) {
        variables.putAll(vari.map { it.name to it })
    }

    fun addMappings(vararg maps: MappedValues) {
        mappings.putAll(maps.map { it.name to it })
    }

    fun build(builder: IacContext.() -> Unit) {
        this.builder()
    }

}
