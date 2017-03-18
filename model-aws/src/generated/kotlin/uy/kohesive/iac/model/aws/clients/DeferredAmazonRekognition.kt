package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.rekognition.AbstractAmazonRekognition
import com.amazonaws.services.rekognition.AmazonRekognition
import com.amazonaws.services.rekognition.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonRekognition(val context: IacContext) : AbstractAmazonRekognition(), AmazonRekognition {

}

class DeferredAmazonRekognition(context: IacContext) : BaseDeferredAmazonRekognition(context)