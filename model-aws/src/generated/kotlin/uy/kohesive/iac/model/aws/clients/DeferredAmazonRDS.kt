package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.rds.AbstractAmazonRDS
import com.amazonaws.services.rds.AmazonRDS
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonRDS(val context: IacContext) : AbstractAmazonRDS(), AmazonRDS {


}

class DeferredAmazonRDS(context: IacContext) : BaseDeferredAmazonRDS(context)
