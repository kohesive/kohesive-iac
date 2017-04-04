package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.codecommit.AWSCodeCommit
import com.amazonaws.services.codecommit.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface CodeCommitIdentifiable : KohesiveIdentifiable {

}

interface CodeCommitEnabled : CodeCommitIdentifiable {
    val codeCommitClient: AWSCodeCommit
    val codeCommitContext: CodeCommitContext
    fun <T> withCodeCommitContext(init: CodeCommitContext.(AWSCodeCommit) -> T): T = codeCommitContext.init(codeCommitClient)
}

open class BaseCodeCommitContext(protected val context: IacContext) : CodeCommitEnabled by context {

    open fun createBranch(branchName: String, init: CreateBranchRequest.() -> Unit): CreateBranchResult {
        return codeCommitClient.createBranch(CreateBranchRequest().apply {
            withBranchName(branchName)
            init()
        })
    }

    open fun createRepository(repositoryName: String, init: CreateRepositoryRequest.() -> Unit): CreateRepositoryResult {
        return codeCommitClient.createRepository(CreateRepositoryRequest().apply {
            withRepositoryName(repositoryName)
            init()
        })
    }


}

@DslScope
class CodeCommitContext(context: IacContext) : BaseCodeCommitContext(context) {

}
