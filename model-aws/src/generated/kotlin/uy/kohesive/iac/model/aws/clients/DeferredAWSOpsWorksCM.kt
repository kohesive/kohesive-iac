package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.opsworkscm.AbstractAWSOpsWorksCM
import com.amazonaws.services.opsworkscm.AWSOpsWorksCM
import com.amazonaws.services.opsworkscm.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSOpsWorksCM(val context: IacContext) : AbstractAWSOpsWorksCM(), AWSOpsWorksCM {

    override fun createBackup(request: CreateBackupRequest): CreateBackupResult {
        return with (context) {
            request.registerWithAutoName()
            CreateBackupResult().withBackup(
                makeProxy<CreateBackupRequest, Backup>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateBackupRequest::getDescription to Backup::getDescription,
                        CreateBackupRequest::getServerName to Backup::getServerName
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createServer(request: CreateServerRequest): CreateServerResult {
        return with (context) {
            request.registerWithAutoName()
            CreateServerResult().withServer(
                makeProxy<CreateServerRequest, Server>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateServerRequest::getAssociatePublicIpAddress to Server::getAssociatePublicIpAddress,
                        CreateServerRequest::getBackupRetentionCount to Server::getBackupRetentionCount,
                        CreateServerRequest::getServerName to Server::getServerName,
                        CreateServerRequest::getDisableAutomatedBackup to Server::getDisableAutomatedBackup,
                        CreateServerRequest::getEngine to Server::getEngine,
                        CreateServerRequest::getEngineModel to Server::getEngineModel,
                        CreateServerRequest::getEngineAttributes to Server::getEngineAttributes,
                        CreateServerRequest::getEngineVersion to Server::getEngineVersion,
                        CreateServerRequest::getInstanceProfileArn to Server::getInstanceProfileArn,
                        CreateServerRequest::getInstanceType to Server::getInstanceType,
                        CreateServerRequest::getKeyPair to Server::getKeyPair,
                        CreateServerRequest::getPreferredMaintenanceWindow to Server::getPreferredMaintenanceWindow,
                        CreateServerRequest::getPreferredBackupWindow to Server::getPreferredBackupWindow,
                        CreateServerRequest::getSecurityGroupIds to Server::getSecurityGroupIds,
                        CreateServerRequest::getServiceRoleArn to Server::getServiceRoleArn,
                        CreateServerRequest::getSubnetIds to Server::getSubnetIds
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }


}

class DeferredAWSOpsWorksCM(context: IacContext) : BaseDeferredAWSOpsWorksCM(context)
