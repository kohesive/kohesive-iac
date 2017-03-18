package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.simpleemail.AbstractAmazonSimpleEmailService
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleemail.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonSimpleEmailService(val context: IacContext) : AbstractAmazonSimpleEmailService(), AmazonSimpleEmailService {

}

class DeferredAmazonSimpleEmailService(context: IacContext) : BaseDeferredAmazonSimpleEmailService(context)