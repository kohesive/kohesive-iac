package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.autoscaling.AbstractAmazonAutoScaling
import com.amazonaws.services.autoscaling.AmazonAutoScaling
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonAutoScaling(val context: IacContext) : AbstractAmazonAutoScaling(), AmazonAutoScaling {

}
