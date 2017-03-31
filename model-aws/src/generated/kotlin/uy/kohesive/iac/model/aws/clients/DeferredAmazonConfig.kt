package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.config.AbstractAmazonConfig
import com.amazonaws.services.config.AmazonConfig
import com.amazonaws.services.config.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonConfig(val context: IacContext) : AbstractAmazonConfig(), AmazonConfig {


}

class DeferredAmazonConfig(context: IacContext) : BaseDeferredAmazonConfig(context)
