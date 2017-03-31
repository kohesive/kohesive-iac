package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.lexruntime.AmazonLexRuntime
import com.amazonaws.services.lexruntime.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface LexRuntimeIdentifiable : KohesiveIdentifiable {

}

interface LexRuntimeEnabled : LexRuntimeIdentifiable {
    val lexRuntimeClient: AmazonLexRuntime
    val lexRuntimeContext: LexRuntimeContext
    fun <T> withLexRuntimeContext(init: LexRuntimeContext.(AmazonLexRuntime) -> T): T = lexRuntimeContext.init(lexRuntimeClient)
}

open class BaseLexRuntimeContext(protected val context: IacContext) : LexRuntimeEnabled by context {


}

@DslScope
class LexRuntimeContext(context: IacContext) : BaseLexRuntimeContext(context) {

}
