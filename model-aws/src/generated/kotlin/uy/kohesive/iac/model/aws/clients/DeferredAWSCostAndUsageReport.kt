package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.costandusagereport.AbstractAWSCostAndUsageReport
import com.amazonaws.services.costandusagereport.AWSCostAndUsageReport
import com.amazonaws.services.costandusagereport.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSCostAndUsageReport(val context: IacContext) : AbstractAWSCostAndUsageReport(), AWSCostAndUsageReport {

}

class DeferredAWSCostAndUsageReport(context: IacContext) : BaseDeferredAWSCostAndUsageReport(context)