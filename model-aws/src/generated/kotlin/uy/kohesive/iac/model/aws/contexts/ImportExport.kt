package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.importexport.AmazonImportExport
import com.amazonaws.services.importexport.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface ImportExportIdentifiable : KohesiveIdentifiable {

}

interface ImportExportEnabled : ImportExportIdentifiable {
    val importExportClient: AmazonImportExport
    val importExportContext: ImportExportContext
    fun <T> withImportExportContext(init: ImportExportContext.(AmazonImportExport) -> T): T = importExportContext.init(importExportClient)
}

open class BaseImportExportContext(protected val context: IacContext) : ImportExportEnabled by context {

}

@DslScope
class ImportExportContext(context: IacContext) : BaseImportExportContext(context) {

}