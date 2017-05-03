package uy.kohesive.iac.model.aws.cloudformation.resources

object CFResources {

    val serviceResourceContainers = listOf(
        ApiGateway::class,
        ApplicationAutoScaling::class,
        AutoScaling::class,
        CertificateManager::class,
        CloudFormation::class,
        CloudFront::class,
        CloudTrail::class,
        CloudWatch::class,
        CodeBuild::class,
        CodeCommit::class,
        CodeDeploy::class,
        CodePipeline::class,
        Config::class,
        DataPipeline::class,
        DirectoryService::class,
        DynamoDB::class,
        EC2::class,
        ECR::class,
        ECS::class,
        EFS::class,
        ElastiCache::class,
        ElasticBeanstalk::class,
        ElasticLoadBalancing::class,
        ElasticLoadBalancingV2::class,
        Elasticsearch::class,
        EMR::class,
        Events::class,
        GameLift::class,
        IAM::class,
        IoT::class,
        Kinesis::class,
        KinesisFirehose::class,
        KMS::class,
        Lambda::class,
        Logs::class,
        OpsWorks::class,
        RDS::class,
        Redshift::class,
        Route53::class,
        S3::class,
        SDB::class,
        SNS::class,
        SQS::class,
        SSM::class,
        StepFunctions::class,
        WAF::class,
        WorkSpaces::class
    )

}