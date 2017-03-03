package uy.kohesive.iac.model.aws

// need to handle implicit variables such as:
// http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/pseudo-parameter-reference.html

enum class ImplicitValues(val type: String) {
    AccountId("AWS::AccountId"),
    NotificationArns("AWS::NotificationARNs"),
    NoValue("AWS::NoValue"),
    Region("AWS::Region"),
    StackId("AWS::StackId"),
    StackName("AWS::StackName");

    fun asRef(): String = "{{kohesive:ivar:$type}}"
    val value: String get() = asRef()
}