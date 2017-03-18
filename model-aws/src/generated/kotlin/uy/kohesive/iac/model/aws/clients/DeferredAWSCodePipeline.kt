package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.codepipeline.AbstractAWSCodePipeline
import com.amazonaws.services.codepipeline.AWSCodePipeline
import com.amazonaws.services.codepipeline.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSCodePipeline(val context: IacContext) : AbstractAWSCodePipeline(), AWSCodePipeline {

}

class DeferredAWSCodePipeline(context: IacContext) : BaseDeferredAWSCodePipeline(context)