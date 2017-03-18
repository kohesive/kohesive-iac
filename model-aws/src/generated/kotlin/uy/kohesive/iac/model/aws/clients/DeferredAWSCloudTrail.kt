package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.cloudtrail.AbstractAWSCloudTrail
import com.amazonaws.services.cloudtrail.AWSCloudTrail
import com.amazonaws.services.cloudtrail.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSCloudTrail(val context: IacContext) : AbstractAWSCloudTrail(), AWSCloudTrail {

}

class DeferredAWSCloudTrail(context: IacContext) : BaseDeferredAWSCloudTrail(context)