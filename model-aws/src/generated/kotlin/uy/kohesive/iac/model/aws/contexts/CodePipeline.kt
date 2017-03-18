package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.codepipeline.AWSCodePipeline
import com.amazonaws.services.codepipeline.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface CodePipelineIdentifiable : KohesiveIdentifiable {

}

interface CodePipelineEnabled : CodePipelineIdentifiable {
    val codePipelineClient: AWSCodePipeline
    val codePipelineContext: CodePipelineContext
    fun <T> withCodePipelineContext(init: CodePipelineContext.(AWSCodePipeline) -> T): T = codePipelineContext.init(codePipelineClient)
}

open class BaseCodePipelineContext(protected val context: IacContext) : CodePipelineEnabled by context {

}

@DslScope
class CodePipelineContext(context: IacContext) : BaseCodePipelineContext(context) {

}