package uy.kohesive.iac.model.aws.contexts

import com.amazonaws.services.ec2.AmazonEC2
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable
import uy.kohesive.iac.model.aws.utils.DslScope

interface Ec2Identifiable : KohesiveIdentifiable {}

interface Ec2Enabled : Ec2Identifiable {
    val ec2Client: AmazonEC2
    val ec2Context: Ec2Context
    fun <T> withEc2Context(init: Ec2Context.(AmazonEC2)->T): T = ec2Context.init(ec2Client)
}

@DslScope
class Ec2Context(private val context: IacContext): Ec2Enabled by context {

}