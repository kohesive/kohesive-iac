package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.codedeploy.AbstractAmazonCodeDeploy
import com.amazonaws.services.codedeploy.AmazonCodeDeploy
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonCodeDeploy(val context: IacContext) : AbstractAmazonCodeDeploy(), AmazonCodeDeploy {


}

class DeferredAmazonCodeDeploy(context: IacContext) : BaseDeferredAmazonCodeDeploy(context)
