package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.codedeploy.AmazonCodeDeploy
import com.amazonaws.services.codedeploy.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface CodeDeployIdentifiable : KohesiveIdentifiable {

}

interface CodeDeployEnabled : CodeDeployIdentifiable {
    val codeDeployClient: AmazonCodeDeploy
    val codeDeployContext: CodeDeployContext
    fun <T> withCodeDeployContext(init: CodeDeployContext.(AmazonCodeDeploy) -> T): T = codeDeployContext.init(codeDeployClient)
}

open class BaseCodeDeployContext(protected val context: IacContext) : CodeDeployEnabled by context {

}

@DslScope
class CodeDeployContext(context: IacContext) : BaseCodeDeployContext(context) {

}