package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.budgets.AbstractAWSBudgets
import com.amazonaws.services.budgets.AWSBudgets
import com.amazonaws.services.budgets.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.proxy.makeProxy

open class BaseDeferredAWSBudgets(val context: IacContext) : AbstractAWSBudgets(), AWSBudgets {

    override fun createBudget(request: CreateBudgetRequest): CreateBudgetResult {
        return with (context) {
            request.registerWithAutoName()
            CreateBudgetResult().registerWithSameNameAs(request)
        }
    }

    override fun createNotification(request: CreateNotificationRequest): CreateNotificationResult {
        return with (context) {
            request.registerWithAutoName()
            CreateNotificationResult().registerWithSameNameAs(request)
        }
    }

    override fun createSubscriber(request: CreateSubscriberRequest): CreateSubscriberResult {
        return with (context) {
            request.registerWithAutoName()
            CreateSubscriberResult().registerWithSameNameAs(request)
        }
    }


}

class DeferredAWSBudgets(context: IacContext) : BaseDeferredAWSBudgets(context)
