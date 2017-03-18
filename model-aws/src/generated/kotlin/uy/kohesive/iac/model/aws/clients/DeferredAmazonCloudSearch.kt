package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.cloudsearchv2.AbstractAmazonCloudSearch
import com.amazonaws.services.cloudsearchv2.AmazonCloudSearch
import com.amazonaws.services.cloudsearchv2.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonCloudSearch(val context: IacContext) : AbstractAmazonCloudSearch(), AmazonCloudSearch {

}

class DeferredAmazonCloudSearch(context: IacContext) : BaseDeferredAmazonCloudSearch(context)