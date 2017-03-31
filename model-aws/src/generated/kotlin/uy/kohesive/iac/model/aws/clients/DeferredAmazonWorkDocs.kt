package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.workdocs.AbstractAmazonWorkDocs
import com.amazonaws.services.workdocs.AmazonWorkDocs
import com.amazonaws.services.workdocs.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonWorkDocs(val context: IacContext) : AbstractAmazonWorkDocs(), AmazonWorkDocs {

    override fun addResourcePermissions(request: AddResourcePermissionsRequest): AddResourcePermissionsResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AddResourcePermissionsRequest, AddResourcePermissionsResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createFolder(request: CreateFolderRequest): CreateFolderResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateFolderRequest, CreateFolderResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createNotificationSubscription(request: CreateNotificationSubscriptionRequest): CreateNotificationSubscriptionResult {
        return with (context) {
            request.registerWithAutoName()
            CreateNotificationSubscriptionResult().withSubscription(
                makeProxy<CreateNotificationSubscriptionRequest, Subscription>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateNotificationSubscriptionRequest::getProtocol to Subscription::getProtocol
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createUser(request: CreateUserRequest): CreateUserResult {
        return with (context) {
            request.registerWithAutoName()
            CreateUserResult().withUser(
                makeProxy<CreateUserRequest, User>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateUserRequest::getUsername to User::getUsername,
                        CreateUserRequest::getGivenName to User::getGivenName,
                        CreateUserRequest::getSurname to User::getSurname,
                        CreateUserRequest::getOrganizationId to User::getOrganizationId,
                        CreateUserRequest::getTimeZoneId to User::getTimeZoneId
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }


}

class DeferredAmazonWorkDocs(context: IacContext) : BaseDeferredAmazonWorkDocs(context)
