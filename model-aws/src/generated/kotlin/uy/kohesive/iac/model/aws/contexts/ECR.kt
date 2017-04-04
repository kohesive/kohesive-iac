package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.ecr.AmazonECR
import com.amazonaws.services.ecr.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface ECRIdentifiable : KohesiveIdentifiable {

}

interface ECREnabled : ECRIdentifiable {
    val ecrClient: AmazonECR
    val ecrContext: ECRContext
    fun <T> withECRContext(init: ECRContext.(AmazonECR) -> T): T = ecrContext.init(ecrClient)
}

open class BaseECRContext(protected val context: IacContext) : ECREnabled by context {

    open fun createRepository(repositoryName: String, init: CreateRepositoryRequest.() -> Unit): Repository {
        return ecrClient.createRepository(CreateRepositoryRequest().apply {
            withRepositoryName(repositoryName)
            init()
        }).getRepository()
    }


}

@DslScope
class ECRContext(context: IacContext) : BaseECRContext(context) {

}
