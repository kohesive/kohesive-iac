package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.inspector.AbstractAmazonInspector
import com.amazonaws.services.inspector.AmazonInspector
import com.amazonaws.services.inspector.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonInspector(val context: IacContext) : AbstractAmazonInspector(), AmazonInspector {

}

class DeferredAmazonInspector(context: IacContext) : BaseDeferredAmazonInspector(context)