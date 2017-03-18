package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.ec2.AbstractAmazonEC2
import com.amazonaws.services.ec2.AmazonEC2
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonEC2(val context: IacContext) : AbstractAmazonEC2(), AmazonEC2 {

}
