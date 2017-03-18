package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.opsworkscm.AbstractAWSOpsWorksCM
import com.amazonaws.services.opsworkscm.AWSOpsWorksCM
import com.amazonaws.services.opsworkscm.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAWSOpsWorksCM(val context: IacContext) : AbstractAWSOpsWorksCM(), AWSOpsWorksCM {

}

class DeferredAWSOpsWorksCM(context: IacContext) : BaseDeferredAWSOpsWorksCM(context)