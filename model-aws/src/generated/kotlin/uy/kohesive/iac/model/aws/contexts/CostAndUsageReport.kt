package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.costandusagereport.AWSCostAndUsageReport
import com.amazonaws.services.costandusagereport.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface CostAndUsageReportIdentifiable : KohesiveIdentifiable {

}

interface CostAndUsageReportEnabled : CostAndUsageReportIdentifiable {
    val costAndUsageReportClient: AWSCostAndUsageReport
    val costAndUsageReportContext: CostAndUsageReportContext
    fun <T> withCostAndUsageReportContext(init: CostAndUsageReportContext.(AWSCostAndUsageReport) -> T): T = costAndUsageReportContext.init(costAndUsageReportClient)
}

open class BaseCostAndUsageReportContext(protected val context: IacContext) : CostAndUsageReportEnabled by context {


}

@DslScope
class CostAndUsageReportContext(context: IacContext) : BaseCostAndUsageReportContext(context) {

}
