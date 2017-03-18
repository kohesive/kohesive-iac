package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.budgets.AbstractAWSBudgets
import com.amazonaws.services.budgets.AWSBudgets
import com.amazonaws.services.budgets.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSBudgets(val context: IacContext) : AbstractAWSBudgets(), AWSBudgets {

}

class DeferredAWSBudgets(context: IacContext) : BaseDeferredAWSBudgets(context)