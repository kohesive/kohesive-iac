package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.workdocs.AbstractAmazonWorkDocs
import com.amazonaws.services.workdocs.AmazonWorkDocs
import com.amazonaws.services.workdocs.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonWorkDocs(val context: IacContext) : AbstractAmazonWorkDocs(), AmazonWorkDocs {

}

class DeferredAmazonWorkDocs(context: IacContext) : BaseDeferredAmazonWorkDocs(context)