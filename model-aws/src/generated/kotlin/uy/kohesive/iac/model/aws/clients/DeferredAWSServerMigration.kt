package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.servermigration.AbstractAWSServerMigration
import com.amazonaws.services.servermigration.AWSServerMigration
import com.amazonaws.services.servermigration.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSServerMigration(val context: IacContext) : AbstractAWSServerMigration(), AWSServerMigration {

    override fun createReplicationJob(request: CreateReplicationJobRequest): CreateReplicationJobResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateReplicationJobRequest, CreateReplicationJobResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAWSServerMigration(context: IacContext) : BaseDeferredAWSServerMigration(context)
