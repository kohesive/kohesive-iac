package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.waf.AbstractAWSWAF
import com.amazonaws.services.waf.AWSWAF
import com.amazonaws.services.waf.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSWAF(val context: IacContext) : AbstractAWSWAF(), AWSWAF {

}

class DeferredAWSWAF(context: IacContext) : BaseDeferredAWSWAF(context)