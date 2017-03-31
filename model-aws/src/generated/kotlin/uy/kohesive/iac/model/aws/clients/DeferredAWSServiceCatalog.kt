package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.servicecatalog.AbstractAWSServiceCatalog
import com.amazonaws.services.servicecatalog.AWSServiceCatalog
import com.amazonaws.services.servicecatalog.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSServiceCatalog(val context: IacContext) : AbstractAWSServiceCatalog(), AWSServiceCatalog {


}

class DeferredAWSServiceCatalog(context: IacContext) : BaseDeferredAWSServiceCatalog(context)
