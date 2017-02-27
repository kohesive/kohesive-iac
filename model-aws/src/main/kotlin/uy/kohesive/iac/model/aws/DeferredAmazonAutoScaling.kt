package uy.kohesive.iac.model.aws

import com.amazonaws.services.autoscaling.AbstractAmazonAutoScaling
import com.amazonaws.services.autoscaling.AmazonAutoScaling

class DeferredAmazonAutoScaling(val context: IacContext) : AbstractAmazonAutoScaling(), AmazonAutoScaling {
    // TODO: implement
}