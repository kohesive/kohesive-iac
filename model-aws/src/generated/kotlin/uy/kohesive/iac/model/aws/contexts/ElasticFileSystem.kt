package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.elasticfilesystem.AmazonElasticFileSystem
import com.amazonaws.services.elasticfilesystem.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface ElasticFileSystemIdentifiable : KohesiveIdentifiable {

}

interface ElasticFileSystemEnabled : ElasticFileSystemIdentifiable {
    val elasticFileSystemClient: AmazonElasticFileSystem
    val elasticFileSystemContext: ElasticFileSystemContext
    fun <T> withElasticFileSystemContext(init: ElasticFileSystemContext.(AmazonElasticFileSystem) -> T): T = elasticFileSystemContext.init(elasticFileSystemClient)
}

open class BaseElasticFileSystemContext(protected val context: IacContext) : ElasticFileSystemEnabled by context {


}

@DslScope
class ElasticFileSystemContext(context: IacContext) : BaseElasticFileSystemContext(context) {

}
