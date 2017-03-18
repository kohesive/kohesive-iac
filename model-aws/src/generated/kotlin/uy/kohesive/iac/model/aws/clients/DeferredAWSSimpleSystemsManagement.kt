package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.simplesystemsmanagement.AbstractAWSSimpleSystemsManagement
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement
import com.amazonaws.services.simplesystemsmanagement.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSSimpleSystemsManagement(val context: IacContext) : AbstractAWSSimpleSystemsManagement(), AWSSimpleSystemsManagement {

}

class DeferredAWSSimpleSystemsManagement(context: IacContext) : BaseDeferredAWSSimpleSystemsManagement(context)