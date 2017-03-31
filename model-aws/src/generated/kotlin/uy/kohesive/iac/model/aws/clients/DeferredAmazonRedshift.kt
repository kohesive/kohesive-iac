package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.redshift.AbstractAmazonRedshift
import com.amazonaws.services.redshift.AmazonRedshift
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonRedshift(val context: IacContext) : AbstractAmazonRedshift(), AmazonRedshift {


}

class DeferredAmazonRedshift(context: IacContext) : BaseDeferredAmazonRedshift(context)
