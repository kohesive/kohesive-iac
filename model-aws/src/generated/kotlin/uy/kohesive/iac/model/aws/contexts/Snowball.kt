package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.snowball.AmazonSnowball
import com.amazonaws.services.snowball.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface SnowballIdentifiable : KohesiveIdentifiable {

}

interface SnowballEnabled : SnowballIdentifiable {
    val snowballClient: AmazonSnowball
    val snowballContext: SnowballContext
    fun <T> withSnowballContext(init: SnowballContext.(AmazonSnowball) -> T): T = snowballContext.init(snowballClient)
}

open class BaseSnowballContext(protected val context: IacContext) : SnowballEnabled by context {


}

@DslScope
class SnowballContext(context: IacContext) : BaseSnowballContext(context) {

}
