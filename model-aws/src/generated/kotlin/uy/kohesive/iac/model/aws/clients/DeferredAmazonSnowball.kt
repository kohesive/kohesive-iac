package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.snowball.AbstractAmazonSnowball
import com.amazonaws.services.snowball.AmazonSnowball
import com.amazonaws.services.snowball.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonSnowball(val context: IacContext) : AbstractAmazonSnowball(), AmazonSnowball {


}

class DeferredAmazonSnowball(context: IacContext) : BaseDeferredAmazonSnowball(context)
