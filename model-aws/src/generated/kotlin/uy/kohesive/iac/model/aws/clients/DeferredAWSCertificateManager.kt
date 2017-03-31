package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.certificatemanager.AbstractAWSCertificateManager
import com.amazonaws.services.certificatemanager.AWSCertificateManager
import com.amazonaws.services.certificatemanager.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSCertificateManager(val context: IacContext) : AbstractAWSCertificateManager(), AWSCertificateManager {

    override fun addTagsToCertificate(request: AddTagsToCertificateRequest): AddTagsToCertificateResult {
        return with (context) {
            request.registerWithAutoName()
            AddTagsToCertificateResult().registerWithSameNameAs(request)
        }
    }


}

class DeferredAWSCertificateManager(context: IacContext) : BaseDeferredAWSCertificateManager(context)
