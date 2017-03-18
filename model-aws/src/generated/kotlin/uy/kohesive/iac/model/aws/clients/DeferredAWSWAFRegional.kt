package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.waf.AbstractAWSWAFRegional
import com.amazonaws.services.waf.AWSWAFRegional
import com.amazonaws.services.waf.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSWAFRegional(val context: IacContext) : AbstractAWSWAFRegional(), AWSWAFRegional {

}

class DeferredAWSWAFRegional(context: IacContext) : BaseDeferredAWSWAFRegional(context)