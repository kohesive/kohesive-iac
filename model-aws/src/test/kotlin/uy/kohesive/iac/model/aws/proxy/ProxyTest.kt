package uy.kohesive.iac.model.aws.proxy

import com.amazonaws.services.ec2.model.Instance
import com.amazonaws.services.ec2.model.Reservation
import com.amazonaws.services.ec2.model.RunInstancesRequest
import com.amazonaws.services.ec2.model.RunInstancesResult
import junit.framework.TestCase
import uy.kohesive.iac.model.aws.IacContext
import kotlin.test.assertFails

class ProxyTest : TestCase() {

    fun testSome() {
        val context = IacContext(
            environment = "test",
            planId      = "somePlan"
        )

        val request = RunInstancesRequest()
            .withImageId("myImageId")
            .withMaxCount(12)

        val result  = RunInstancesResult().withReservation(
            Reservation().withInstances(
                makeProxy<RunInstancesRequest, Instance>(context, "MyInstance", request, mapOf(
                        RunInstancesRequest::getImageId to Instance::getImageId
                    ),
                    disallowReferences = listOf(Instance::getArchitecture)
                )
            )
        )

        assertNotNull(result.reservation.instances.firstOrNull())
        val instance = result.reservation.instances[0]

        // Id
        assertEquals("MyInstance", context.getId(instance))

        // Copied property
        assertEquals("myImageId", instance.imageId)

        // New ref-property
        assertEquals("{{kohesive:ref:Instance:MyInstance:PublicDnsName}}", instance.publicDnsName)

        // Disallowed property
        assertFails {
            println(instance.architecture)
        }
    }

}