package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.elastictranscoder.AbstractAmazonElasticTranscoder
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoder
import com.amazonaws.services.elastictranscoder.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonElasticTranscoder(val context: IacContext) : AbstractAmazonElasticTranscoder(), AmazonElasticTranscoder {

}

class DeferredAmazonElasticTranscoder(context: IacContext) : BaseDeferredAmazonElasticTranscoder(context)