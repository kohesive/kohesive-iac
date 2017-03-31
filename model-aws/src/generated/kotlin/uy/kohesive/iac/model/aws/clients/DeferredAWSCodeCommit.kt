package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.codecommit.AbstractAWSCodeCommit
import com.amazonaws.services.codecommit.AWSCodeCommit
import com.amazonaws.services.codecommit.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSCodeCommit(val context: IacContext) : AbstractAWSCodeCommit(), AWSCodeCommit {


}

class DeferredAWSCodeCommit(context: IacContext) : BaseDeferredAWSCodeCommit(context)
