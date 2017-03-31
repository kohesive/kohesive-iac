package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.lambda.AbstractAWSLambda
import com.amazonaws.services.lambda.AWSLambda
import com.amazonaws.services.lambda.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSLambda(val context: IacContext) : AbstractAWSLambda(), AWSLambda {

    override fun addPermission(request: AddPermissionRequest): AddPermissionResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AddPermissionRequest, AddPermissionResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createAlias(request: CreateAliasRequest): CreateAliasResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateAliasRequest, CreateAliasResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateAliasRequest::getName to CreateAliasResult::getName,
                    CreateAliasRequest::getFunctionVersion to CreateAliasResult::getFunctionVersion,
                    CreateAliasRequest::getDescription to CreateAliasResult::getDescription
                )
            )
        }
    }

    override fun createEventSourceMapping(request: CreateEventSourceMappingRequest): CreateEventSourceMappingResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateEventSourceMappingRequest, CreateEventSourceMappingResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateEventSourceMappingRequest::getBatchSize to CreateEventSourceMappingResult::getBatchSize,
                    CreateEventSourceMappingRequest::getEventSourceArn to CreateEventSourceMappingResult::getEventSourceArn
                )
            )
        }
    }

    override fun createFunction(request: CreateFunctionRequest): CreateFunctionResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateFunctionRequest, CreateFunctionResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request,
                copyFromReq   = mapOf(
                    CreateFunctionRequest::getFunctionName to CreateFunctionResult::getFunctionName,
                    CreateFunctionRequest::getRuntime to CreateFunctionResult::getRuntime,
                    CreateFunctionRequest::getRole to CreateFunctionResult::getRole,
                    CreateFunctionRequest::getHandler to CreateFunctionResult::getHandler,
                    CreateFunctionRequest::getDescription to CreateFunctionResult::getDescription,
                    CreateFunctionRequest::getTimeout to CreateFunctionResult::getTimeout,
                    CreateFunctionRequest::getMemorySize to CreateFunctionResult::getMemorySize,
                    CreateFunctionRequest::getDeadLetterConfig to CreateFunctionResult::getDeadLetterConfig,
                    CreateFunctionRequest::getKMSKeyArn to CreateFunctionResult::getKMSKeyArn
                )
            )
        }
    }


}

class DeferredAWSLambda(context: IacContext) : BaseDeferredAWSLambda(context)
