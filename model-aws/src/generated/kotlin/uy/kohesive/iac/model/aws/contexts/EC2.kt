package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.model.*
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface EC2Identifiable : KohesiveIdentifiable {

}

interface EC2Enabled : EC2Identifiable {
    val ec2Client: AmazonEC2
    val ec2Context: EC2Context
    fun <T> withEC2Context(init: EC2Context.(AmazonEC2) -> T): T = ec2Context.init(ec2Client)
}

open class BaseEC2Context(protected val context: IacContext) : EC2Enabled by context {

    fun createImage(name: String, init: CreateImageRequest.() -> Unit): CreateImageResult {
        return ec2Client.createImage(CreateImageRequest().apply {
            withName(name)
            init()
        })
    }


}

