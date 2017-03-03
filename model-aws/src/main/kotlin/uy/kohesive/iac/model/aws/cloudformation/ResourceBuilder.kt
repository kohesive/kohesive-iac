package uy.kohesive.iac.model.aws.cloudformation

import com.amazonaws.AmazonWebServiceRequest
import uy.kohesive.iac.model.aws.AwsTypes
import kotlin.reflect.KClass

// TODO: I failed to use generics here :(
object ResourcePropertyBuilders {

    private val awsTypeToBuilder: Map<AwsTypes, ResourcePropertiesBuilder<*>> = mapOf(
        AwsTypes.IamRole             to IamRoleResourcePropertiesBuilder(),
        AwsTypes.IamPolicy           to IamPolicyResourcePropertiesBuilder(),
        AwsTypes.IamInstanceProfile  to IamInstanceProfilePropertiesBuilder(),
        AwsTypes.LaunchConfiguration to LaunchConfigurationPropertiesBuilder(),
        AwsTypes.AutoScalingGroup    to AutoScalingGroupPropertiesBuilder()
    )

    fun getBuilder(awsType: AwsTypes) = awsTypeToBuilder[awsType]

}

interface CloudFormationResourceProperties

interface ResourcePropertiesBuilder<T : AmazonWebServiceRequest> {
    val requestClazz: KClass<T>
    fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>): ResourceProperties
}