package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.cloudhsm.AbstractAWSCloudHSM
import com.amazonaws.services.cloudhsm.AWSCloudHSM
import com.amazonaws.services.cloudhsm.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSCloudHSM(val context: IacContext) : AbstractAWSCloudHSM(), AWSCloudHSM {

    override fun addTagsToResource(request: AddTagsToResourceRequest): AddTagsToResourceResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<AddTagsToResourceRequest, AddTagsToResourceResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createHapg(request: CreateHapgRequest): CreateHapgResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateHapgRequest, CreateHapgResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createHsm(request: CreateHsmRequest): CreateHsmResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateHsmRequest, CreateHsmResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }

    override fun createLunaClient(request: CreateLunaClientRequest): CreateLunaClientResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateLunaClientRequest, CreateLunaClientResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAWSCloudHSM(context: IacContext) : BaseDeferredAWSCloudHSM(context)
