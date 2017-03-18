package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.databasemigrationservice.AbstractAWSDatabaseMigrationService
import com.amazonaws.services.databasemigrationservice.AWSDatabaseMigrationService
import com.amazonaws.services.databasemigrationservice.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSDatabaseMigrationService(val context: IacContext) : AbstractAWSDatabaseMigrationService(), AWSDatabaseMigrationService {

}

class DeferredAWSDatabaseMigrationService(context: IacContext) : BaseDeferredAWSDatabaseMigrationService(context)