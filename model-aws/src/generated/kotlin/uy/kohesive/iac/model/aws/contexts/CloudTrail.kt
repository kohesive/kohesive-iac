package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.cloudtrail.AWSCloudTrail
import com.amazonaws.services.cloudtrail.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface CloudTrailIdentifiable : KohesiveIdentifiable {

}

interface CloudTrailEnabled : CloudTrailIdentifiable {
    val cloudTrailClient: AWSCloudTrail
    val cloudTrailContext: CloudTrailContext
    fun <T> withCloudTrailContext(init: CloudTrailContext.(AWSCloudTrail) -> T): T = cloudTrailContext.init(cloudTrailClient)
}

open class BaseCloudTrailContext(protected val context: IacContext) : CloudTrailEnabled by context {

    fun createTrail(name: String, init: CreateTrailRequest.() -> Unit): CreateTrailResult {
        return cloudTrailClient.createTrail(CreateTrailRequest().apply {
            withName(name)
            init()
        })
    }


}

@DslScope
class CloudTrailContext(context: IacContext) : BaseCloudTrailContext(context) {

}
