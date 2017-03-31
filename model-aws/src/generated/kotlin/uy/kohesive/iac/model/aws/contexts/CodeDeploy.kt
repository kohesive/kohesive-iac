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

    fun createApplication(applicationName: String, init: CreateApplicationRequest.() -> Unit): CreateApplicationResult {
        return codeDeployClient.createApplication(CreateApplicationRequest().apply {
            withApplicationName(applicationName)
            init()
        })
    }

    fun createDeploymentConfig(deploymentConfigName: String, init: CreateDeploymentConfigRequest.() -> Unit): CreateDeploymentConfigResult {
        return codeDeployClient.createDeploymentConfig(CreateDeploymentConfigRequest().apply {
            withDeploymentConfigName(deploymentConfigName)
            init()
        })
    }

    fun createDeploymentGroup(deploymentGroupName: String, init: CreateDeploymentGroupRequest.() -> Unit): CreateDeploymentGroupResult {
        return codeDeployClient.createDeploymentGroup(CreateDeploymentGroupRequest().apply {
            withDeploymentGroupName(deploymentGroupName)
            init()
        })
    }


}

@DslScope
class CodeDeployContext(context: IacContext) : BaseCodeDeployContext(context) {

}
