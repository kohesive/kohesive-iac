package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.servicecatalog.AbstractAWSServiceCatalog
import com.amazonaws.services.servicecatalog.AWSServiceCatalog
import com.amazonaws.services.servicecatalog.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSServiceCatalog(val context: IacContext) : AbstractAWSServiceCatalog(), AWSServiceCatalog {

}

class DeferredAWSServiceCatalog(context: IacContext) : BaseDeferredAWSServiceCatalog(context)