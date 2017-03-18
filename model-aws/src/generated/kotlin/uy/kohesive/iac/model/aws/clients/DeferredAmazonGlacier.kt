package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.glacier.AbstractAmazonGlacier
import com.amazonaws.services.glacier.AmazonGlacier
import com.amazonaws.services.glacier.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonGlacier(val context: IacContext) : AbstractAmazonGlacier(), AmazonGlacier {

}

class DeferredAmazonGlacier(context: IacContext) : BaseDeferredAmazonGlacier(context)