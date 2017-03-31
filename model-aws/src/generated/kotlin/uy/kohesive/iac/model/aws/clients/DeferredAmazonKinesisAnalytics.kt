package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.kinesisanalytics.AbstractAmazonKinesisAnalytics
import com.amazonaws.services.kinesisanalytics.AmazonKinesisAnalytics
import com.amazonaws.services.kinesisanalytics.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonKinesisAnalytics(val context: IacContext) : AbstractAmazonKinesisAnalytics(), AmazonKinesisAnalytics {

    override fun addApplicationInput(request: AddApplicationInputRequest): AddApplicationInputResult {
        return with (context) {
            request.registerWithAutoName()
            AddApplicationInputResult().registerWithSameNameAs(request)
        }
    }

    override fun addApplicationOutput(request: AddApplicationOutputRequest): AddApplicationOutputResult {
        return with (context) {
            request.registerWithAutoName()
            AddApplicationOutputResult().registerWithSameNameAs(request)
        }
    }

    override fun addApplicationReferenceDataSource(request: AddApplicationReferenceDataSourceRequest): AddApplicationReferenceDataSourceResult {
        return with (context) {
            request.registerWithAutoName()
            AddApplicationReferenceDataSourceResult().registerWithSameNameAs(request)
        }
    }

    override fun createApplication(request: CreateApplicationRequest): CreateApplicationResult {
        return with (context) {
            request.registerWithAutoName()
            makeProxy<CreateApplicationRequest, CreateApplicationResult>(
                context       = this@with,
                sourceName    = getNameStrict(request),
                requestObject = request
            )
        }
    }


}

class DeferredAmazonKinesisAnalytics(context: IacContext) : BaseDeferredAmazonKinesisAnalytics(context)
