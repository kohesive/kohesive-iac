package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.cloudfront.AbstractAmazonCloudFront
import com.amazonaws.services.cloudfront.AmazonCloudFront
import com.amazonaws.services.cloudfront.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonCloudFront(val context: IacContext) : AbstractAmazonCloudFront(), AmazonCloudFront {

}

class DeferredAmazonCloudFront(context: IacContext) : BaseDeferredAmazonCloudFront(context)