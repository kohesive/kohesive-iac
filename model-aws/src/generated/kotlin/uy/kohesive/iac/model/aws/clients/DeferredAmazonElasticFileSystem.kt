package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.elasticfilesystem.AbstractAmazonElasticFileSystem
import com.amazonaws.services.elasticfilesystem.AmazonElasticFileSystem
import com.amazonaws.services.elasticfilesystem.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonElasticFileSystem(val context: IacContext) : AbstractAmazonElasticFileSystem(), AmazonElasticFileSystem {

}

class DeferredAmazonElasticFileSystem(context: IacContext) : BaseDeferredAmazonElasticFileSystem(context)