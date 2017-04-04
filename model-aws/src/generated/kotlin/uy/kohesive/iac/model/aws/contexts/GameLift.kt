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

    open fun createAlias(name: String, init: CreateAliasRequest.() -> Unit): Alias {
        return gameLiftClient.createAlias(CreateAliasRequest().apply {
            withName(name)
            init()
        }).getAlias()
    }

    open fun createBuild(name: String, init: CreateBuildRequest.() -> Unit): Build {
        return gameLiftClient.createBuild(CreateBuildRequest().apply {
            withName(name)
            init()
        }).getBuild()
    }

    open fun createFleet(name: String, init: CreateFleetRequest.() -> Unit): CreateFleetResult {
        return gameLiftClient.createFleet(CreateFleetRequest().apply {
            withName(name)
            init()
        })
    }

    open fun createGameSession(name: String, init: CreateGameSessionRequest.() -> Unit): GameSession {
        return gameLiftClient.createGameSession(CreateGameSessionRequest().apply {
            withName(name)
            init()
        }).getGameSession()
    }

    open fun createGameSessionQueue(name: String, init: CreateGameSessionQueueRequest.() -> Unit): GameSessionQueue {
        return gameLiftClient.createGameSessionQueue(CreateGameSessionQueueRequest().apply {
            withName(name)
            init()
        }).getGameSessionQueue()
    }


}

@DslScope
class GameLiftContext(context: IacContext) : BaseGameLiftContext(context) {

}
