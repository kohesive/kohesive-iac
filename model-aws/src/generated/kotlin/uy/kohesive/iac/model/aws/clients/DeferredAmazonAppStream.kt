package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.appstream.AbstractAmazonAppStream
import com.amazonaws.services.appstream.AmazonAppStream
import com.amazonaws.services.appstream.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonAppStream(val context: IacContext) : AbstractAmazonAppStream(), AmazonAppStream {

}

class DeferredAmazonAppStream(context: IacContext) : BaseDeferredAmazonAppStream(context)