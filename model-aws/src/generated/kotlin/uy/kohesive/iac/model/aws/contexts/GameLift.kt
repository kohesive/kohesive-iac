package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.gamelift.AmazonGameLift
import com.amazonaws.services.gamelift.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface GameLiftIdentifiable : KohesiveIdentifiable {

}

interface GameLiftEnabled : GameLiftIdentifiable {
    val gameLiftClient: AmazonGameLift
    val gameLiftContext: GameLiftContext
    fun <T> withGameLiftContext(init: GameLiftContext.(AmazonGameLift) -> T): T = gameLiftContext.init(gameLiftClient)
}

open class BaseGameLiftContext(protected val context: IacContext) : GameLiftEnabled by context {

}

@DslScope
class GameLiftContext(context: IacContext) : BaseGameLiftContext(context) {

}