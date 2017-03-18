package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.importexport.AbstractAmazonImportExport
import com.amazonaws.services.importexport.AmazonImportExport
import com.amazonaws.services.importexport.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonImportExport(val context: IacContext) : AbstractAmazonImportExport(), AmazonImportExport {

}

class DeferredAmazonImportExport(context: IacContext) : BaseDeferredAmazonImportExport(context)