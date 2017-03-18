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

}

@DslScope
class CodeCommitContext(context: IacContext) : BaseCodeCommitContext(context) {

}