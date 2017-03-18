package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.ecr.AbstractAmazonECR
import com.amazonaws.services.ecr.AmazonECR
import com.amazonaws.services.ecr.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonECR(val context: IacContext) : AbstractAmazonECR(), AmazonECR {

}

class DeferredAmazonECR(context: IacContext) : BaseDeferredAmazonECR(context)