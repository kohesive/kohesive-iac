package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.organizations.AbstractAWSOrganizations
import com.amazonaws.services.organizations.AWSOrganizations
import com.amazonaws.services.organizations.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSOrganizations(val context: IacContext) : AbstractAWSOrganizations(), AWSOrganizations {

    override fun createOrganization(request: CreateOrganizationRequest): CreateOrganizationResult {
        return with (context) {
            request.registerWithAutoName()
            CreateOrganizationResult().withOrganization(
                makeProxy<CreateOrganizationRequest, Organization>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateOrganizationRequest::getMode to Organization::getMode
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createOrganizationalUnit(request: CreateOrganizationalUnitRequest): CreateOrganizationalUnitResult {
        return with (context) {
            request.registerWithAutoName()
            CreateOrganizationalUnitResult().withOrganizationalUnit(
                makeProxy<CreateOrganizationalUnitRequest, OrganizationalUnit>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreateOrganizationalUnitRequest::getName to OrganizationalUnit::getName
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }

    override fun createPolicy(request: CreatePolicyRequest): CreatePolicyResult {
        return with (context) {
            request.registerWithAutoName()
            CreatePolicyResult().withPolicy(
                makeProxy<CreatePolicyRequest, Policy>(
                    context       = this@with,
                    sourceName    = getNameStrict(request),
                    requestObject = request,
                    copyFromReq   = mapOf(
                        CreatePolicyRequest::getContent to Policy::getContent
                    )
                )
            ).registerWithSameNameAs(request)
        }
    }


}

class DeferredAWSOrganizations(context: IacContext) : BaseDeferredAWSOrganizations(context)
