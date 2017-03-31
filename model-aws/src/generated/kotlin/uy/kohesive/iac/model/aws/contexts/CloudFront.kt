package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.cloudfront.AmazonCloudFront
import com.amazonaws.services.cloudfront.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface CloudFrontIdentifiable : KohesiveIdentifiable {

}

interface CloudFrontEnabled : CloudFrontIdentifiable {
    val cloudFrontClient: AmazonCloudFront
    val cloudFrontContext: CloudFrontContext
    fun <T> withCloudFrontContext(init: CloudFrontContext.(AmazonCloudFront) -> T): T = cloudFrontContext.init(cloudFrontClient)
}

open class BaseCloudFrontContext(protected val context: IacContext) : CloudFrontEnabled by context {


}

@DslScope
class CloudFrontContext(context: IacContext) : BaseCloudFrontContext(context) {

}
