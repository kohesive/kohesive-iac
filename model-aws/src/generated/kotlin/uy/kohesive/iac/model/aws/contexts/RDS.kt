package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.rds.AmazonRDS
import com.amazonaws.services.rds.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface RDSIdentifiable : KohesiveIdentifiable {

}

interface RDSEnabled : RDSIdentifiable {
    val rdsClient: AmazonRDS
    val rdsContext: RDSContext
    fun <T> withRDSContext(init: RDSContext.(AmazonRDS) -> T): T = rdsContext.init(rdsClient)
}

open class BaseRDSContext(protected val context: IacContext) : RDSEnabled by context {

    fun createDBClusterParameterGroup(dBClusterParameterGroupName: String, init: CreateDBClusterParameterGroupRequest.() -> Unit): DBClusterParameterGroup {
        return rdsClient.createDBClusterParameterGroup(CreateDBClusterParameterGroupRequest().apply {
            withDBClusterParameterGroupName(dBClusterParameterGroupName)
            init()
        })
    }

    fun createDBParameterGroup(dBParameterGroupName: String, init: CreateDBParameterGroupRequest.() -> Unit): DBParameterGroup {
        return rdsClient.createDBParameterGroup(CreateDBParameterGroupRequest().apply {
            withDBParameterGroupName(dBParameterGroupName)
            init()
        })
    }

    fun createDBSecurityGroup(dBSecurityGroupName: String, init: CreateDBSecurityGroupRequest.() -> Unit): DBSecurityGroup {
        return rdsClient.createDBSecurityGroup(CreateDBSecurityGroupRequest().apply {
            withDBSecurityGroupName(dBSecurityGroupName)
            init()
        })
    }

    fun createDBSubnetGroup(dBSubnetGroupName: String, init: CreateDBSubnetGroupRequest.() -> Unit): DBSubnetGroup {
        return rdsClient.createDBSubnetGroup(CreateDBSubnetGroupRequest().apply {
            withDBSubnetGroupName(dBSubnetGroupName)
            init()
        })
    }

    fun createOptionGroup(optionGroupName: String, init: CreateOptionGroupRequest.() -> Unit): OptionGroup {
        return rdsClient.createOptionGroup(CreateOptionGroupRequest().apply {
            withOptionGroupName(optionGroupName)
            init()
        })
    }


}

@DslScope
class RDSContext(context: IacContext) : BaseRDSContext(context) {

}
