package uy.kohesive.iac.model.aws.cloudformation

import com.amazonaws.AmazonWebServiceRequest
import uy.kohesive.iac.model.aws.AwsTypes
import uy.kohesive.iac.model.aws.cloudformation.resources.builders.*
import kotlin.reflect.KClass

// TODO: I failed to use generics here :(
object ResourcePropertyBuilders {

    private val awsTypeToBuilder: Map<AwsTypes, ResourcePropertiesBuilder<*>> = mapOf(
        AwsTypes.IamRole             to IamRoleResourcePropertiesBuilder(),
        AwsTypes.IamPolicy           to IamPolicyResourcePropertiesBuilder(),
        AwsTypes.IamInstanceProfile  to IamInstanceProfilePropertiesBuilder(),
        AwsTypes.LaunchConfiguration to LaunchConfigurationPropertiesBuilder(),
        AwsTypes.AutoScalingGroup    to AutoScalingGroupPropertiesBuilder(),
        AwsTypes.Ec2SecurityGroup    to Ec2SecurityGroupPropertiesBuilder(),
        AwsTypes.DynamoDBTable       to DynamoDBTableResourcePropertiesBuilder(),
        AwsTypes.WaitCondition       to WaitConditionPropertiesBuilder()
    )

    fun getBuilder(awsType: AwsTypes) = awsTypeToBuilder[awsType]

}

interface ResourcePropertiesBuilder<T : AmazonWebServiceRequest> {

    val requestClazz: KClass<T>

    fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>): ResourceProperties

    fun canBuildFrom(request: AmazonWebServiceRequest): Boolean = true

}