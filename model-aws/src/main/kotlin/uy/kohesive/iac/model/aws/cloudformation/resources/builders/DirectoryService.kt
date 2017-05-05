package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.directory.model.CreateDirectoryRequest
import com.amazonaws.services.directory.model.CreateMicrosoftADRequest
import com.amazonaws.services.directory.model.EnableSsoRequest
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.DirectoryService

class DirectoryServiceMicrosoftADResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateMicrosoftADRequest> {

    override val requestClazz = CreateMicrosoftADRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateMicrosoftADRequest).let {
            DirectoryService.MicrosoftAD(
                Name        = request.name,
                Password    = request.password,
                ShortName   = request.shortName,
                VpcSettings = request.vpcSettings.let {
                    DirectoryService.MicrosoftAD.VpcSettingProperty(
                        SubnetIds = it.subnetIds,
                        VpcId     = it.vpcId
                    )
                },
                EnableSso = relatedObjects.filterIsInstance<EnableSsoRequest>().isNotEmpty().toString()
            )
        }

}

class DirectoryServiceSimpleADResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateDirectoryRequest> {

    override val requestClazz = CreateDirectoryRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateDirectoryRequest).let {
            DirectoryService.SimpleAD(
                Name        = request.name,
                Password    = request.password,
                ShortName   = request.shortName,
                VpcSettings = request.vpcSettings.let {
                    DirectoryService.SimpleAD.VpcSettingProperty(
                        SubnetIds = it.subnetIds,
                        VpcId     = it.vpcId
                    )
                },
                EnableSso   = relatedObjects.filterIsInstance<EnableSsoRequest>().isNotEmpty().toString(),
                Size        = request.size,
                Description = request.description
            )
        }

}

