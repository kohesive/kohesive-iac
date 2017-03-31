package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.codecommit.AbstractAWSCodeCommit
import com.amazonaws.services.codecommit.AWSCodeCommit
import com.amazonaws.services.codecommit.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSCodeCommit(val context: IacContext) : AbstractAWSCodeCommit(), AWSCodeCommit {

    override fun createBranch(request: CreateBranchRequest): CreateBranchResult {
        return with (context) {
            request.registerWithAutoName()
            CreateBranchResult().registerWithSameNameAs(request)
        }
    }

    override fun createRepository(request: CreateRepositoryRequest): CreateRepositoryResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateRepositoryRequest, CreateRepositoryResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAWSCodeCommit(context: IacContext) : BaseDeferredAWSCodeCommit(context)
