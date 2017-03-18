package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.budgets.AWSBudgets
import com.amazonaws.services.budgets.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface BudgetsIdentifiable : KohesiveIdentifiable {

}

interface BudgetsEnabled : BudgetsIdentifiable {
    val budgetsClient: AWSBudgets
    val budgetsContext: BudgetsContext
    fun <T> withBudgetsContext(init: BudgetsContext.(AWSBudgets) -> T): T = budgetsContext.init(budgetsClient)
}

open class BaseBudgetsContext(protected val context: IacContext) : BudgetsEnabled by context {

}

@DslScope
class BudgetsContext(context: IacContext) : BaseBudgetsContext(context) {

}