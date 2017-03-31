package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.gamelift.AbstractAmazonGameLift
import com.amazonaws.services.gamelift.AmazonGameLift
import com.amazonaws.services.gamelift.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonGameLift(val context: IacContext) : AbstractAmazonGameLift(), AmazonGameLift {

    override fun createAlias(request: CreateAliasRequest): CreateAliasResult {
        return with (context) {
            request.registerWithAutoName()
            CreateAliasResult().withAlias(
                makeProxy<CreateAliasRequest, Alias>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateAliasRequest::getName to Alias::getName,
                        CreateAliasRequest::getDescription to Alias::getDescription,
                        CreateAliasRequest::getRoutingStrategy to Alias::getRoutingStrategy
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createBuild(request: CreateBuildRequest): CreateBuildResult {
        return with (context) {
            request.registerWithAutoName()
            CreateBuildResult().withBuild(
                makeProxy<CreateBuildRequest, Build>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateBuildRequest::getName to Build::getName,
                        CreateBuildRequest::getVersion to Build::getVersion,
                        CreateBuildRequest::getOperatingSystem to Build::getOperatingSystem
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createFleet(request: CreateFleetRequest): CreateFleetResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateFleetRequest, CreateFleetResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createGameSession(request: CreateGameSessionRequest): CreateGameSessionResult {
        return with (context) {
            request.registerWithAutoName()
            CreateGameSessionResult().withGameSession(
                makeProxy<CreateGameSessionRequest, GameSession>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateGameSessionRequest::getGameSessionId to GameSession::getGameSessionId,
                        CreateGameSessionRequest::getName to GameSession::getName,
                        CreateGameSessionRequest::getFleetId to GameSession::getFleetId,
                        CreateGameSessionRequest::getMaximumPlayerSessionCount to GameSession::getMaximumPlayerSessionCount,
                        CreateGameSessionRequest::getGameProperties to GameSession::getGameProperties,
                        CreateGameSessionRequest::getCreatorId to GameSession::getCreatorId
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createGameSessionQueue(request: CreateGameSessionQueueRequest): CreateGameSessionQueueResult {
        return with (context) {
            request.registerWithAutoName()
            CreateGameSessionQueueResult().withGameSessionQueue(
                makeProxy<CreateGameSessionQueueRequest, GameSessionQueue>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateGameSessionQueueRequest::getName to GameSessionQueue::getName,
                        CreateGameSessionQueueRequest::getTimeoutInSeconds to GameSessionQueue::getTimeoutInSeconds,
                        CreateGameSessionQueueRequest::getDestinations to GameSessionQueue::getDestinations
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createPlayerSession(request: CreatePlayerSessionRequest): CreatePlayerSessionResult {
        return with (context) {
            request.registerWithAutoName()
            CreatePlayerSessionResult().withPlayerSession(
                makeProxy<CreatePlayerSessionRequest, PlayerSession>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreatePlayerSessionRequest::getPlayerId to PlayerSession::getPlayerId,
                        CreatePlayerSessionRequest::getGameSessionId to PlayerSession::getGameSessionId,
                        CreatePlayerSessionRequest::getPlayerData to PlayerSession::getPlayerData
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createPlayerSessions(request: CreatePlayerSessionsRequest): CreatePlayerSessionsResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreatePlayerSessionsRequest, CreatePlayerSessionsResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAmazonGameLift(context: IacContext) : BaseDeferredAmazonGameLift(context)
