package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.clouddirectory.AbstractAmazonCloudDirectory
import com.amazonaws.services.clouddirectory.AmazonCloudDirectory
import com.amazonaws.services.clouddirectory.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonCloudDirectory(val context: IacContext) : AbstractAmazonCloudDirectory(), AmazonCloudDirectory {

}

class DeferredAmazonCloudDirectory(context: IacContext) : BaseDeferredAmazonCloudDirectory(context)