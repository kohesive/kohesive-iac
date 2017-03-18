package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.config.AbstractAmazonConfig
import com.amazonaws.services.config.AmazonConfig
import com.amazonaws.services.config.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonConfig(val context: IacContext) : AbstractAmazonConfig(), AmazonConfig {

}

class DeferredAmazonConfig(context: IacContext) : BaseDeferredAmazonConfig(context)