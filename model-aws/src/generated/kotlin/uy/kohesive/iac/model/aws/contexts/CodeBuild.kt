package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.codebuild.AWSCodeBuild
import com.amazonaws.services.codebuild.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface CodeBuildIdentifiable : KohesiveIdentifiable {

}

interface CodeBuildEnabled : CodeBuildIdentifiable {
    val codeBuildClient: AWSCodeBuild
    val codeBuildContext: CodeBuildContext
    fun <T> withCodeBuildContext(init: CodeBuildContext.(AWSCodeBuild) -> T): T = codeBuildContext.init(codeBuildClient)
}

open class BaseCodeBuildContext(protected val context: IacContext) : CodeBuildEnabled by context {

}

@DslScope
class CodeBuildContext(context: IacContext) : BaseCodeBuildContext(context) {

}