package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.cloudhsm.AWSCloudHSM
import com.amazonaws.services.cloudhsm.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface CloudHSMIdentifiable : KohesiveIdentifiable {

}

interface CloudHSMEnabled : CloudHSMIdentifiable {
    val cloudHSMClient: AWSCloudHSM
    val cloudHSMContext: CloudHSMContext
    fun <T> withCloudHSMContext(init: CloudHSMContext.(AWSCloudHSM) -> T): T = cloudHSMContext.init(cloudHSMClient)
}

open class BaseCloudHSMContext(protected val context: IacContext) : CloudHSMEnabled by context {


}

@DslScope
class CloudHSMContext(context: IacContext) : BaseCloudHSMContext(context) {

}
