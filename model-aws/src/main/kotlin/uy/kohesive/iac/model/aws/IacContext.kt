package uy.kohesive.iac.model.aws

//// @DslScope
//open class IacContext(
//    val environment: String,
//    val planId: String,
//    val namingStrategy: IacContextNamingStrategy = IacSimpleEnvPrefixNamingStrategy(),
//    init: IacContext.() -> Unit = {}
//) : BaseIacContext(), Ec2Enabled, IamRoleEnabled, AutoScalingEnabled, DynamoDbEnabled {
//
//    override val ec2Client: AmazonEC2 by lazy { DeferredAmazonEC2(this) }
//    override val ec2Context: Ec2Context by lazy { Ec2Context(this) }
//    override val iamClient: AmazonIdentityManagement by lazy { DeferredAmazonIdentityManagement(this) }
//    override val iamContext: IamContext by lazy { IamContext(this) }
//    override val autoScalingClient: AmazonAutoScaling by lazy { DeferredAmazonAutoScaling(this) }
//    override val autoScalingContext: AutoScalingContext by lazy { AutoScalingContext(this) }
//    override val dynamoDbClient: AmazonDynamoDB by lazy { DeferredAmazonDynamoDB(this) }
//    override val dynamoDbContext: DynamoDbContext by lazy { DynamoDbContext(this) }
//
//    init {
//        init()
//    }
//
//}
