package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.codebuild.AbstractAWSCodeBuild
import com.amazonaws.services.codebuild.AWSCodeBuild
import com.amazonaws.services.codebuild.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSCodeBuild(val context: IacContext) : AbstractAWSCodeBuild(), AWSCodeBuild {

}

class DeferredAWSCodeBuild(context: IacContext) : BaseDeferredAWSCodeBuild(context)