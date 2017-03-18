package uy.kohesive.iac.model.aws.clients

import com.amazonaws.services.simpledb.AbstractAmazonSimpleDB
import com.amazonaws.services.simpledb.AmazonSimpleDB
import com.amazonaws.services.simpledb.model.*
import uy.kohesive.iac.model.aws.IacContext

open class BaseDeferredAmazonSimpleDB(val context: IacContext) : AbstractAmazonSimpleDB(), AmazonSimpleDB {

}

class DeferredAmazonSimpleDB(context: IacContext) : BaseDeferredAmazonSimpleDB(context)