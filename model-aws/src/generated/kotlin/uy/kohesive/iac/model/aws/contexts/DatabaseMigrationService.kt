package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.databasemigrationservice.AWSDatabaseMigrationService
import com.amazonaws.services.databasemigrationservice.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface DatabaseMigrationServiceIdentifiable : KohesiveIdentifiable {

}

interface DatabaseMigrationServiceEnabled : DatabaseMigrationServiceIdentifiable {
    val databaseMigrationServiceClient: AWSDatabaseMigrationService
    val databaseMigrationServiceContext: DatabaseMigrationServiceContext
    fun <T> withDatabaseMigrationServiceContext(init: DatabaseMigrationServiceContext.(AWSDatabaseMigrationService) -> T): T = databaseMigrationServiceContext.init(databaseMigrationServiceClient)
}

open class BaseDatabaseMigrationServiceContext(protected val context: IacContext) : DatabaseMigrationServiceEnabled by context {


}

@DslScope
class DatabaseMigrationServiceContext(context: IacContext) : BaseDatabaseMigrationServiceContext(context) {

}
