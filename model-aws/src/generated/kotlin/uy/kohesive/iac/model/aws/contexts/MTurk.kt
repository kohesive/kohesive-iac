package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.mturk.AmazonMTurk
import com.amazonaws.services.mturk.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface MTurkIdentifiable : KohesiveIdentifiable {

}

interface MTurkEnabled : MTurkIdentifiable {
    val mTurkClient: AmazonMTurk
    val mTurkContext: MTurkContext
    fun <T> withMTurkContext(init: MTurkContext.(AmazonMTurk) -> T): T = mTurkContext.init(mTurkClient)
}

open class BaseMTurkContext(protected val context: IacContext) : MTurkEnabled by context {

    open fun createQualificationType(name: String, init: CreateQualificationTypeRequest.() -> Unit): QualificationType {
        return mTurkClient.createQualificationType(CreateQualificationTypeRequest().apply {
            withName(name)
            init()
        }).getQualificationType()
    }


}

@DslScope
class MTurkContext(context: IacContext) : BaseMTurkContext(context) {

}
