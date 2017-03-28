package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object CertificateManager {

    @CloudFormationType("AWS::CertificateManager::Certificate")
    data class Certificate(
        val DomainName: String,
        val DomainValidationOptions: List<CertificateManager.Certificate.DomainValidationOptionProperty>? = null,
        val SubjectAlternativeNames: List<String>? = null,
        val Tags: List<CloudFormation.ResourceTag>? = null
    ) : ResourceProperties {

        data class DomainValidationOptionProperty(
            val DomainName: String,
            val ValidationDomain: String
        ) 

    }


}