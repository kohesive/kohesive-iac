package uy.kohesive.iac.model.aws

import uy.kohesive.iac.model.aws.clients.*
import uy.kohesive.iac.model.aws.contexts.*
import com.amazonaws.services.applicationautoscaling.AWSApplicationAutoScaling
import com.amazonaws.services.applicationdiscovery.AWSApplicationDiscovery
import com.amazonaws.services.batch.AWSBatch
import com.amazonaws.services.budgets.AWSBudgets
import com.amazonaws.services.certificatemanager.AWSCertificateManager
import com.amazonaws.services.cloudhsm.AWSCloudHSM
import com.amazonaws.services.cloudtrail.AWSCloudTrail
import com.amazonaws.services.codebuild.AWSCodeBuild
import com.amazonaws.services.codecommit.AWSCodeCommit
import com.amazonaws.services.codepipeline.AWSCodePipeline
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider
import com.amazonaws.services.costandusagereport.AWSCostAndUsageReport
import com.amazonaws.services.databasemigrationservice.AWSDatabaseMigrationService
import com.amazonaws.services.devicefarm.AWSDeviceFarm
import com.amazonaws.services.directory.AWSDirectoryService
import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalk
import com.amazonaws.services.elasticsearch.AWSElasticsearch
import com.amazonaws.services.health.AWSHealth
import com.amazonaws.services.iot.AWSIot
import com.amazonaws.services.iotdata.AWSIotData
import com.amazonaws.services.kms.AWSKMS
import com.amazonaws.services.lambda.AWSLambda
import com.amazonaws.services.logs.AWSLogs
import com.amazonaws.services.marketplacecommerceanalytics.AWSMarketplaceCommerceAnalytics
import com.amazonaws.services.marketplacemetering.AWSMarketplaceMetering
import com.amazonaws.services.opsworks.AWSOpsWorks
import com.amazonaws.services.opsworkscm.AWSOpsWorksCM
import com.amazonaws.services.organizations.AWSOrganizations
import com.amazonaws.services.securitytoken.AWSSecurityTokenService
import com.amazonaws.services.servermigration.AWSServerMigration
import com.amazonaws.services.servicecatalog.AWSServiceCatalog
import com.amazonaws.services.shield.AWSShield
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement
import com.amazonaws.services.stepfunctions.AWSStepFunctions
import com.amazonaws.services.storagegateway.AWSStorageGateway
import com.amazonaws.services.support.AWSSupport
import com.amazonaws.services.waf.AWSWAF
import com.amazonaws.services.waf.AWSWAFRegional
import com.amazonaws.services.xray.AWSXRay
import com.amazonaws.services.apigateway.AmazonApiGateway
import com.amazonaws.services.appstream.AmazonAppStream
import com.amazonaws.services.autoscaling.AmazonAutoScaling
import com.amazonaws.services.clouddirectory.AmazonCloudDirectory
import com.amazonaws.services.cloudformation.AmazonCloudFormation
import com.amazonaws.services.cloudfront.AmazonCloudFront
import com.amazonaws.services.cloudsearchv2.AmazonCloudSearch
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomain
import com.amazonaws.services.cloudwatch.AmazonCloudWatch
import com.amazonaws.services.cloudwatchevents.AmazonCloudWatchEvents
import com.amazonaws.services.codedeploy.AmazonCodeDeploy
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentity
import com.amazonaws.services.cognitosync.AmazonCognitoSync
import com.amazonaws.services.config.AmazonConfig
import com.amazonaws.services.directconnect.AmazonDirectConnect
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBStreams
import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ecr.AmazonECR
import com.amazonaws.services.ecs.AmazonECS
import com.amazonaws.services.elasticache.AmazonElastiCache
import com.amazonaws.services.elasticfilesystem.AmazonElasticFileSystem
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce
import com.amazonaws.services.elastictranscoder.AmazonElasticTranscoder
import com.amazonaws.services.gamelift.AmazonGameLift
import com.amazonaws.services.glacier.AmazonGlacier
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement
import com.amazonaws.services.importexport.AmazonImportExport
import com.amazonaws.services.inspector.AmazonInspector
import com.amazonaws.services.kinesis.AmazonKinesis
import com.amazonaws.services.kinesisanalytics.AmazonKinesisAnalytics
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose
import com.amazonaws.services.lexruntime.AmazonLexRuntime
import com.amazonaws.services.lightsail.AmazonLightsail
import com.amazonaws.services.mturk.AmazonMTurk
import com.amazonaws.services.machinelearning.AmazonMachineLearning
import com.amazonaws.services.pinpoint.AmazonPinpoint
import com.amazonaws.services.polly.AmazonPolly
import com.amazonaws.services.rds.AmazonRDS
import com.amazonaws.services.redshift.AmazonRedshift
import com.amazonaws.services.rekognition.AmazonRekognition
import com.amazonaws.services.route53.AmazonRoute53
import com.amazonaws.services.route53domains.AmazonRoute53Domains
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.simpledb.AmazonSimpleDB
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow
import com.amazonaws.services.snowball.AmazonSnowball
import com.amazonaws.services.workdocs.AmazonWorkDocs
import com.amazonaws.services.workspaces.AmazonWorkspaces
import com.amazonaws.services.datapipeline.DataPipeline

// @DslScope
open class IacContext(
    val environment: String,
    val planId: String,
    val namingStrategy: IacContextNamingStrategy = IacSimpleEnvPrefixNamingStrategy(),
    init: IacContext.() -> Unit = {}
): BaseIacContext(), ApiGatewayEnabled, AppStreamEnabled, ApplicationAutoScalingEnabled, ApplicationDiscoveryEnabled, AutoScalingEnabled, BatchEnabled, BudgetsEnabled, CertificateManagerEnabled, CloudDirectoryEnabled, CloudFormationEnabled, CloudFrontEnabled, CloudHSMEnabled, CloudSearchEnabled, CloudSearchDomainEnabled, CloudTrailEnabled, CloudWatchEnabled, CloudWatchEventsEnabled, CodeBuildEnabled, CodeCommitEnabled, CodeDeployEnabled, CodePipelineEnabled, CognitoIdentityEnabled, CognitoIdentityProviderEnabled, CognitoSyncEnabled, ConfigEnabled, CostAndUsageReportEnabled, DataPipelineEnabled, DatabaseMigrationServiceEnabled, DeviceFarmEnabled, DirectConnectEnabled, DirectoryServiceEnabled, DynamoDBEnabled, DynamoDBStreamsEnabled, EC2Enabled, ECREnabled, ECSEnabled, ElastiCacheEnabled, ElasticBeanstalkEnabled, ElasticFileSystemEnabled, ElasticLoadBalancingEnabled, ElasticLoadBalancingV2Enabled, ElasticMapReduceEnabled, ElasticTranscoderEnabled, ElasticsearchEnabled, GameLiftEnabled, GlacierEnabled, HealthEnabled, IdentityManagementEnabled, ImportExportEnabled, InspectorEnabled, IotEnabled, IotDataEnabled, KMSEnabled, KinesisAnalyticsEnabled, KinesisEnabled, KinesisFirehoseEnabled, LambdaEnabled, LexRuntimeEnabled, LightsailEnabled, LogsEnabled, MTurkEnabled, MachineLearningEnabled, MarketplaceCommerceAnalyticsEnabled, MarketplaceMeteringEnabled, OpsWorksCMEnabled, OpsWorksEnabled, OrganizationsEnabled, PinpointEnabled, PollyEnabled, RDSEnabled, RedshiftEnabled, RekognitionEnabled, Route53Enabled, Route53DomainsEnabled, SNSEnabled, SQSEnabled, SecurityTokenServiceEnabled, ServerMigrationEnabled, ServiceCatalogEnabled, ShieldEnabled, SimpleDBEnabled, SimpleEmailServiceEnabled, SimpleSystemsManagementEnabled, SimpleWorkflowEnabled, SnowballEnabled, StepFunctionsEnabled, StorageGatewayEnabled, SupportEnabled, WAFEnabled, WAFRegionalEnabled, WorkDocsEnabled, WorkspacesEnabled, XRayEnabled {

    // Clients
    override val applicationAutoScalingClient: AWSApplicationAutoScaling by lazy { DeferredAWSApplicationAutoScaling(this) }
    override val applicationDiscoveryClient: AWSApplicationDiscovery by lazy { DeferredAWSApplicationDiscovery(this) }
    override val batchClient: AWSBatch by lazy { DeferredAWSBatch(this) }
    override val budgetsClient: AWSBudgets by lazy { DeferredAWSBudgets(this) }
    override val certificateManagerClient: AWSCertificateManager by lazy { DeferredAWSCertificateManager(this) }
    override val cloudHSMClient: AWSCloudHSM by lazy { DeferredAWSCloudHSM(this) }
    override val cloudTrailClient: AWSCloudTrail by lazy { DeferredAWSCloudTrail(this) }
    override val codeBuildClient: AWSCodeBuild by lazy { DeferredAWSCodeBuild(this) }
    override val codeCommitClient: AWSCodeCommit by lazy { DeferredAWSCodeCommit(this) }
    override val codePipelineClient: AWSCodePipeline by lazy { DeferredAWSCodePipeline(this) }
    override val cognitoIdentityProviderClient: AWSCognitoIdentityProvider by lazy { DeferredAWSCognitoIdentityProvider(this) }
    override val costAndUsageReportClient: AWSCostAndUsageReport by lazy { DeferredAWSCostAndUsageReport(this) }
    override val databaseMigrationServiceClient: AWSDatabaseMigrationService by lazy { DeferredAWSDatabaseMigrationService(this) }
    override val deviceFarmClient: AWSDeviceFarm by lazy { DeferredAWSDeviceFarm(this) }
    override val directoryServiceClient: AWSDirectoryService by lazy { DeferredAWSDirectoryService(this) }
    override val elasticBeanstalkClient: AWSElasticBeanstalk by lazy { DeferredAWSElasticBeanstalk(this) }
    override val elasticsearchClient: AWSElasticsearch by lazy { DeferredAWSElasticsearch(this) }
    override val healthClient: AWSHealth by lazy { DeferredAWSHealth(this) }
    override val iotClient: AWSIot by lazy { DeferredAWSIot(this) }
    override val iotDataClient: AWSIotData by lazy { DeferredAWSIotData(this) }
    override val kmsClient: AWSKMS by lazy { DeferredAWSKMS(this) }
    override val lambdaClient: AWSLambda by lazy { DeferredAWSLambda(this) }
    override val logsClient: AWSLogs by lazy { DeferredAWSLogs(this) }
    override val marketplaceCommerceAnalyticsClient: AWSMarketplaceCommerceAnalytics by lazy { DeferredAWSMarketplaceCommerceAnalytics(this) }
    override val marketplaceMeteringClient: AWSMarketplaceMetering by lazy { DeferredAWSMarketplaceMetering(this) }
    override val opsWorksClient: AWSOpsWorks by lazy { DeferredAWSOpsWorks(this) }
    override val opsWorksCMClient: AWSOpsWorksCM by lazy { DeferredAWSOpsWorksCM(this) }
    override val organizationsClient: AWSOrganizations by lazy { DeferredAWSOrganizations(this) }
    override val securityTokenServiceClient: AWSSecurityTokenService by lazy { DeferredAWSSecurityTokenService(this) }
    override val serverMigrationClient: AWSServerMigration by lazy { DeferredAWSServerMigration(this) }
    override val serviceCatalogClient: AWSServiceCatalog by lazy { DeferredAWSServiceCatalog(this) }
    override val shieldClient: AWSShield by lazy { DeferredAWSShield(this) }
    override val simpleSystemsManagementClient: AWSSimpleSystemsManagement by lazy { DeferredAWSSimpleSystemsManagement(this) }
    override val stepFunctionsClient: AWSStepFunctions by lazy { DeferredAWSStepFunctions(this) }
    override val storageGatewayClient: AWSStorageGateway by lazy { DeferredAWSStorageGateway(this) }
    override val supportClient: AWSSupport by lazy { DeferredAWSSupport(this) }
    override val wafClient: AWSWAF by lazy { DeferredAWSWAF(this) }
    override val wAFRegionalClient: AWSWAFRegional by lazy { DeferredAWSWAFRegional(this) }
    override val xRayClient: AWSXRay by lazy { DeferredAWSXRay(this) }
    override val apiGatewayClient: AmazonApiGateway by lazy { DeferredAmazonApiGateway(this) }
    override val appStreamClient: AmazonAppStream by lazy { DeferredAmazonAppStream(this) }
    override val autoScalingClient: AmazonAutoScaling by lazy { DeferredAmazonAutoScaling(this) }
    override val cloudDirectoryClient: AmazonCloudDirectory by lazy { DeferredAmazonCloudDirectory(this) }
    override val cloudFormationClient: AmazonCloudFormation by lazy { DeferredAmazonCloudFormation(this) }
    override val cloudFrontClient: AmazonCloudFront by lazy { DeferredAmazonCloudFront(this) }
    override val cloudSearchClient: AmazonCloudSearch by lazy { DeferredAmazonCloudSearch(this) }
    override val cloudSearchDomainClient: AmazonCloudSearchDomain by lazy { DeferredAmazonCloudSearchDomain(this) }
    override val cloudWatchClient: AmazonCloudWatch by lazy { DeferredAmazonCloudWatch(this) }
    override val cloudWatchEventsClient: AmazonCloudWatchEvents by lazy { DeferredAmazonCloudWatchEvents(this) }
    override val codeDeployClient: AmazonCodeDeploy by lazy { DeferredAmazonCodeDeploy(this) }
    override val cognitoIdentityClient: AmazonCognitoIdentity by lazy { DeferredAmazonCognitoIdentity(this) }
    override val cognitoSyncClient: AmazonCognitoSync by lazy { DeferredAmazonCognitoSync(this) }
    override val configClient: AmazonConfig by lazy { DeferredAmazonConfig(this) }
    override val directConnectClient: AmazonDirectConnect by lazy { DeferredAmazonDirectConnect(this) }
    override val dynamoDBClient: AmazonDynamoDB by lazy { DeferredAmazonDynamoDB(this) }
    override val dynamoDBStreamsClient: AmazonDynamoDBStreams by lazy { DeferredAmazonDynamoDBStreams(this) }
    override val ec2Client: AmazonEC2 by lazy { DeferredAmazonEC2(this) }
    override val ecrClient: AmazonECR by lazy { DeferredAmazonECR(this) }
    override val ecsClient: AmazonECS by lazy { DeferredAmazonECS(this) }
    override val elastiCacheClient: AmazonElastiCache by lazy { DeferredAmazonElastiCache(this) }
    override val elasticFileSystemClient: AmazonElasticFileSystem by lazy { DeferredAmazonElasticFileSystem(this) }
    override val elasticLoadBalancingClient: AmazonElasticLoadBalancing by lazy { DeferredAmazonElasticLoadBalancing(this) }
    override val elasticLoadBalancingV2Client: com.amazonaws.services.elasticloadbalancingv2.AmazonElasticLoadBalancing by lazy { DeferredAmazonElasticLoadBalancingV2(this) }
    override val elasticMapReduceClient: AmazonElasticMapReduce by lazy { DeferredAmazonElasticMapReduce(this) }
    override val elasticTranscoderClient: AmazonElasticTranscoder by lazy { DeferredAmazonElasticTranscoder(this) }
    override val gameLiftClient: AmazonGameLift by lazy { DeferredAmazonGameLift(this) }
    override val glacierClient: AmazonGlacier by lazy { DeferredAmazonGlacier(this) }
    override val identityManagementClient: AmazonIdentityManagement by lazy { DeferredAmazonIdentityManagement(this) }
    override val importExportClient: AmazonImportExport by lazy { DeferredAmazonImportExport(this) }
    override val inspectorClient: AmazonInspector by lazy { DeferredAmazonInspector(this) }
    override val kinesisClient: AmazonKinesis by lazy { DeferredAmazonKinesis(this) }
    override val kinesisAnalyticsClient: AmazonKinesisAnalytics by lazy { DeferredAmazonKinesisAnalytics(this) }
    override val kinesisFirehoseClient: AmazonKinesisFirehose by lazy { DeferredAmazonKinesisFirehose(this) }
    override val lexRuntimeClient: AmazonLexRuntime by lazy { DeferredAmazonLexRuntime(this) }
    override val lightsailClient: AmazonLightsail by lazy { DeferredAmazonLightsail(this) }
    override val mTurkClient: AmazonMTurk by lazy { DeferredAmazonMTurk(this) }
    override val machineLearningClient: AmazonMachineLearning by lazy { DeferredAmazonMachineLearning(this) }
    override val pinpointClient: AmazonPinpoint by lazy { DeferredAmazonPinpoint(this) }
    override val pollyClient: AmazonPolly by lazy { DeferredAmazonPolly(this) }
    override val rdsClient: AmazonRDS by lazy { DeferredAmazonRDS(this) }
    override val redshiftClient: AmazonRedshift by lazy { DeferredAmazonRedshift(this) }
    override val rekognitionClient: AmazonRekognition by lazy { DeferredAmazonRekognition(this) }
    override val route53Client: AmazonRoute53 by lazy { DeferredAmazonRoute53(this) }
    override val route53DomainsClient: AmazonRoute53Domains by lazy { DeferredAmazonRoute53Domains(this) }
    override val snsClient: AmazonSNS by lazy { DeferredAmazonSNS(this) }
    override val sqsClient: AmazonSQS by lazy { DeferredAmazonSQS(this) }
    override val simpleDBClient: AmazonSimpleDB by lazy { DeferredAmazonSimpleDB(this) }
    override val simpleEmailServiceClient: AmazonSimpleEmailService by lazy { DeferredAmazonSimpleEmailService(this) }
    override val simpleWorkflowClient: AmazonSimpleWorkflow by lazy { DeferredAmazonSimpleWorkflow(this) }
    override val snowballClient: AmazonSnowball by lazy { DeferredAmazonSnowball(this) }
    override val workDocsClient: AmazonWorkDocs by lazy { DeferredAmazonWorkDocs(this) }
    override val workspacesClient: AmazonWorkspaces by lazy { DeferredAmazonWorkspaces(this) }
    override val dataPipelineClient: DataPipeline by lazy { DeferredDataPipeline(this) }

    // Contexts
    override val apiGatewayContext: ApiGatewayContext by lazy { ApiGatewayContext(this) }
    override val appStreamContext: AppStreamContext by lazy { AppStreamContext(this) }
    override val applicationAutoScalingContext: ApplicationAutoScalingContext by lazy { ApplicationAutoScalingContext(this) }
    override val applicationDiscoveryContext: ApplicationDiscoveryContext by lazy { ApplicationDiscoveryContext(this) }
    override val autoScalingContext: AutoScalingContext by lazy { AutoScalingContext(this) }
    override val batchContext: BatchContext by lazy { BatchContext(this) }
    override val budgetsContext: BudgetsContext by lazy { BudgetsContext(this) }
    override val certificateManagerContext: CertificateManagerContext by lazy { CertificateManagerContext(this) }
    override val cloudDirectoryContext: CloudDirectoryContext by lazy { CloudDirectoryContext(this) }
    override val cloudFormationContext: CloudFormationContext by lazy { CloudFormationContext(this) }
    override val cloudFrontContext: CloudFrontContext by lazy { CloudFrontContext(this) }
    override val cloudHSMContext: CloudHSMContext by lazy { CloudHSMContext(this) }
    override val cloudSearchContext: CloudSearchContext by lazy { CloudSearchContext(this) }
    override val cloudSearchDomainContext: CloudSearchDomainContext by lazy { CloudSearchDomainContext(this) }
    override val cloudTrailContext: CloudTrailContext by lazy { CloudTrailContext(this) }
    override val cloudWatchContext: CloudWatchContext by lazy { CloudWatchContext(this) }
    override val cloudWatchEventsContext: CloudWatchEventsContext by lazy { CloudWatchEventsContext(this) }
    override val codeBuildContext: CodeBuildContext by lazy { CodeBuildContext(this) }
    override val codeCommitContext: CodeCommitContext by lazy { CodeCommitContext(this) }
    override val codeDeployContext: CodeDeployContext by lazy { CodeDeployContext(this) }
    override val codePipelineContext: CodePipelineContext by lazy { CodePipelineContext(this) }
    override val cognitoIdentityContext: CognitoIdentityContext by lazy { CognitoIdentityContext(this) }
    override val cognitoIdentityProviderContext: CognitoIdentityProviderContext by lazy { CognitoIdentityProviderContext(this) }
    override val cognitoSyncContext: CognitoSyncContext by lazy { CognitoSyncContext(this) }
    override val configContext: ConfigContext by lazy { ConfigContext(this) }
    override val costAndUsageReportContext: CostAndUsageReportContext by lazy { CostAndUsageReportContext(this) }
    override val dataPipelineContext: DataPipelineContext by lazy { DataPipelineContext(this) }
    override val databaseMigrationServiceContext: DatabaseMigrationServiceContext by lazy { DatabaseMigrationServiceContext(this) }
    override val deviceFarmContext: DeviceFarmContext by lazy { DeviceFarmContext(this) }
    override val directConnectContext: DirectConnectContext by lazy { DirectConnectContext(this) }
    override val directoryServiceContext: DirectoryServiceContext by lazy { DirectoryServiceContext(this) }
    override val dynamoDBContext: DynamoDBContext by lazy { DynamoDBContext(this) }
    override val dynamoDBStreamsContext: DynamoDBStreamsContext by lazy { DynamoDBStreamsContext(this) }
    override val ec2Context: EC2Context by lazy { EC2Context(this) }
    override val ecrContext: ECRContext by lazy { ECRContext(this) }
    override val ecsContext: ECSContext by lazy { ECSContext(this) }
    override val elastiCacheContext: ElastiCacheContext by lazy { ElastiCacheContext(this) }
    override val elasticBeanstalkContext: ElasticBeanstalkContext by lazy { ElasticBeanstalkContext(this) }
    override val elasticFileSystemContext: ElasticFileSystemContext by lazy { ElasticFileSystemContext(this) }
    override val elasticLoadBalancingContext: ElasticLoadBalancingContext by lazy { ElasticLoadBalancingContext(this) }
    override val elasticLoadBalancingV2Context: ElasticLoadBalancingV2Context by lazy { ElasticLoadBalancingV2Context(this) }
    override val elasticMapReduceContext: ElasticMapReduceContext by lazy { ElasticMapReduceContext(this) }
    override val elasticTranscoderContext: ElasticTranscoderContext by lazy { ElasticTranscoderContext(this) }
    override val elasticsearchContext: ElasticsearchContext by lazy { ElasticsearchContext(this) }
    override val gameLiftContext: GameLiftContext by lazy { GameLiftContext(this) }
    override val glacierContext: GlacierContext by lazy { GlacierContext(this) }
    override val healthContext: HealthContext by lazy { HealthContext(this) }
    override val identityManagementContext: IdentityManagementContext by lazy { IdentityManagementContext(this) }
    override val importExportContext: ImportExportContext by lazy { ImportExportContext(this) }
    override val inspectorContext: InspectorContext by lazy { InspectorContext(this) }
    override val iotContext: IotContext by lazy { IotContext(this) }
    override val iotDataContext: IotDataContext by lazy { IotDataContext(this) }
    override val kmsContext: KMSContext by lazy { KMSContext(this) }
    override val kinesisAnalyticsContext: KinesisAnalyticsContext by lazy { KinesisAnalyticsContext(this) }
    override val kinesisContext: KinesisContext by lazy { KinesisContext(this) }
    override val kinesisFirehoseContext: KinesisFirehoseContext by lazy { KinesisFirehoseContext(this) }
    override val lambdaContext: LambdaContext by lazy { LambdaContext(this) }
    override val lexRuntimeContext: LexRuntimeContext by lazy { LexRuntimeContext(this) }
    override val lightsailContext: LightsailContext by lazy { LightsailContext(this) }
    override val logsContext: LogsContext by lazy { LogsContext(this) }
    override val mTurkContext: MTurkContext by lazy { MTurkContext(this) }
    override val machineLearningContext: MachineLearningContext by lazy { MachineLearningContext(this) }
    override val marketplaceCommerceAnalyticsContext: MarketplaceCommerceAnalyticsContext by lazy { MarketplaceCommerceAnalyticsContext(this) }
    override val marketplaceMeteringContext: MarketplaceMeteringContext by lazy { MarketplaceMeteringContext(this) }
    override val opsWorksCMContext: OpsWorksCMContext by lazy { OpsWorksCMContext(this) }
    override val opsWorksContext: OpsWorksContext by lazy { OpsWorksContext(this) }
    override val organizationsContext: OrganizationsContext by lazy { OrganizationsContext(this) }
    override val pinpointContext: PinpointContext by lazy { PinpointContext(this) }
    override val pollyContext: PollyContext by lazy { PollyContext(this) }
    override val rdsContext: RDSContext by lazy { RDSContext(this) }
    override val redshiftContext: RedshiftContext by lazy { RedshiftContext(this) }
    override val rekognitionContext: RekognitionContext by lazy { RekognitionContext(this) }
    override val route53Context: Route53Context by lazy { Route53Context(this) }
    override val route53DomainsContext: Route53DomainsContext by lazy { Route53DomainsContext(this) }
    override val snsContext: SNSContext by lazy { SNSContext(this) }
    override val sqsContext: SQSContext by lazy { SQSContext(this) }
    override val securityTokenServiceContext: SecurityTokenServiceContext by lazy { SecurityTokenServiceContext(this) }
    override val serverMigrationContext: ServerMigrationContext by lazy { ServerMigrationContext(this) }
    override val serviceCatalogContext: ServiceCatalogContext by lazy { ServiceCatalogContext(this) }
    override val shieldContext: ShieldContext by lazy { ShieldContext(this) }
    override val simpleDBContext: SimpleDBContext by lazy { SimpleDBContext(this) }
    override val simpleEmailServiceContext: SimpleEmailServiceContext by lazy { SimpleEmailServiceContext(this) }
    override val simpleSystemsManagementContext: SimpleSystemsManagementContext by lazy { SimpleSystemsManagementContext(this) }
    override val simpleWorkflowContext: SimpleWorkflowContext by lazy { SimpleWorkflowContext(this) }
    override val snowballContext: SnowballContext by lazy { SnowballContext(this) }
    override val stepFunctionsContext: StepFunctionsContext by lazy { StepFunctionsContext(this) }
    override val storageGatewayContext: StorageGatewayContext by lazy { StorageGatewayContext(this) }
    override val supportContext: SupportContext by lazy { SupportContext(this) }
    override val wafContext: WAFContext by lazy { WAFContext(this) }
    override val wAFRegionalContext: WAFRegionalContext by lazy { WAFRegionalContext(this) }
    override val workDocsContext: WorkDocsContext by lazy { WorkDocsContext(this) }
    override val workspacesContext: WorkspacesContext by lazy { WorkspacesContext(this) }
    override val xRayContext: XRayContext by lazy { XRayContext(this) }

    init {
        init()
    }

}