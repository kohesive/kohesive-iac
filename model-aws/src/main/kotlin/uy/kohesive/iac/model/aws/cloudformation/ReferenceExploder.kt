package uy.kohesive.iac.model.aws.cloudformation

import uy.kohesive.iac.model.aws.proxy.createReference

class One(val value: Any)
class Two(val value: Any)
class Three(val value: Any)

fun main(args: Array<String>) {
    val one   = createReference<One>("one")
    val two   = createReference<Two>(one)
    val three = createReference<Three>(two)
}

class ReferenceExploder() {

}