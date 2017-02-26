package uy.kohesive.iac.model.aws.helpers

import com.amazonaws.services.ec2.AmazonEC2
import uy.kohesive.iac.model.aws.IacContext
import uy.kohesive.iac.model.aws.KohesiveIdentifiable

interface Ec2Identifiable : KohesiveIdentifiable {}

interface Ec2Enabled : Ec2Identifiable {
    val ec2Client: AmazonEC2
    val ec2Context: Ec2Context
    fun withEc2Context(init: Ec2Context.(AmazonEC2)->Unit) = ec2Context.init(ec2Client)
}

class Ec2Context(private val context: IacContext): Ec2Enabled by context