package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.servermigration.AWSServerMigration
import com.amazonaws.services.servermigration.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface ServerMigrationIdentifiable : KohesiveIdentifiable {

}

interface ServerMigrationEnabled : ServerMigrationIdentifiable {
    val serverMigrationClient: AWSServerMigration
    val serverMigrationContext: ServerMigrationContext
    fun <T> withServerMigrationContext(init: ServerMigrationContext.(AWSServerMigration) -> T): T = serverMigrationContext.init(serverMigrationClient)
}

open class BaseServerMigrationContext(protected val context: IacContext) : ServerMigrationEnabled by context {

}

@DslScope
class ServerMigrationContext(context: IacContext) : BaseServerMigrationContext(context) {

}