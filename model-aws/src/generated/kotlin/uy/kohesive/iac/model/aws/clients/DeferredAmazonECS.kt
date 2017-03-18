package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.ecs.AbstractAmazonECS
import com.amazonaws.services.ecs.AmazonECS
import com.amazonaws.services.ecs.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonECS(val context: IacContext) : AbstractAmazonECS(), AmazonECS {

}

class DeferredAmazonECS(context: IacContext) : BaseDeferredAmazonECS(context)