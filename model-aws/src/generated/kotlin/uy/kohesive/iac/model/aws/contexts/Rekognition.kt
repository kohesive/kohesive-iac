package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.rekognition.AmazonRekognition
import com.amazonaws.services.rekognition.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface RekognitionIdentifiable : KohesiveIdentifiable {

}

interface RekognitionEnabled : RekognitionIdentifiable {
    val rekognitionClient: AmazonRekognition
    val rekognitionContext: RekognitionContext
    fun <T> withRekognitionContext(init: RekognitionContext.(AmazonRekognition) -> T): T = rekognitionContext.init(rekognitionClient)
}

open class BaseRekognitionContext(protected val context: IacContext) : RekognitionEnabled by context {

}

@DslScope
class RekognitionContext(context: IacContext) : BaseRekognitionContext(context) {

}