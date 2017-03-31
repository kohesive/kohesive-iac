package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.certificatemanager.AWSCertificateManager
import com.amazonaws.services.certificatemanager.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface CertificateManagerIdentifiable : KohesiveIdentifiable {

}

interface CertificateManagerEnabled : CertificateManagerIdentifiable {
    val certificateManagerClient: AWSCertificateManager
    val certificateManagerContext: CertificateManagerContext
    fun <T> withCertificateManagerContext(init: CertificateManagerContext.(AWSCertificateManager) -> T): T = certificateManagerContext.init(certificateManagerClient)
}

open class BaseCertificateManagerContext(protected val context: IacContext) : CertificateManagerEnabled by context {


}

@DslScope
class CertificateManagerContext(context: IacContext) : BaseCertificateManagerContext(context) {

}
