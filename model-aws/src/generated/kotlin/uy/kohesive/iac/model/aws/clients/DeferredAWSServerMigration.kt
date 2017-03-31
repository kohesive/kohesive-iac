package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.servermigration.AbstractAWSServerMigration
import com.amazonaws.services.servermigration.AWSServerMigration
import com.amazonaws.services.servermigration.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSServerMigration(val context: IacContext) : AbstractAWSServerMigration(), AWSServerMigration {


}

class DeferredAWSServerMigration(context: IacContext) : BaseDeferredAWSServerMigration(context)
