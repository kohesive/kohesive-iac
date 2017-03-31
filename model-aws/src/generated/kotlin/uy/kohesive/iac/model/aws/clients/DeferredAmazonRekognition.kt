package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.rekognition.AbstractAmazonRekognition
import com.amazonaws.services.rekognition.AmazonRekognition
import com.amazonaws.services.rekognition.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonRekognition(val context: IacContext) : AbstractAmazonRekognition(), AmazonRekognition {

    override fun createCollection(request: CreateCollectionRequest): CreateCollectionResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateCollectionRequest, CreateCollectionResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAmazonRekognition(context: IacContext) : BaseDeferredAmazonRekognition(context)
