package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.simpleemail.AbstractAmazonSimpleEmailService
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleemail.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAmazonSimpleEmailService(val context: IacContext) : AbstractAmazonSimpleEmailService(), AmazonSimpleEmailService {

    override fun createConfigurationSet(request: CreateConfigurationSetRequest): CreateConfigurationSetResult {
        return with (context) {
            request.registerWithAutoName()
            CreateConfigurationSetResult().registerWithSameNameAs(request)
        }
    }

    override fun createConfigurationSetEventDestination(request: CreateConfigurationSetEventDestinationRequest): CreateConfigurationSetEventDestinationResult {
        return with (context) {
            request.registerWithAutoName()
            CreateConfigurationSetEventDestinationResult().registerWithSameNameAs(request)
        }
    }

    override fun createReceiptFilter(request: CreateReceiptFilterRequest): CreateReceiptFilterResult {
        return with (context) {
            request.registerWithAutoName()
            CreateReceiptFilterResult().registerWithSameNameAs(request)
        }
    }

    override fun createReceiptRule(request: CreateReceiptRuleRequest): CreateReceiptRuleResult {
        return with (context) {
            request.registerWithAutoName()
            CreateReceiptRuleResult().registerWithSameNameAs(request)
        }
    }

    override fun createReceiptRuleSet(request: CreateReceiptRuleSetRequest): CreateReceiptRuleSetResult {
        return with (context) {
            request.registerWithAutoName()
            CreateReceiptRuleSetResult().registerWithSameNameAs(request)
        }
    }


}

class DeferredAmazonSimpleEmailService(context: IacContext) : BaseDeferredAmazonSimpleEmailService(context)
