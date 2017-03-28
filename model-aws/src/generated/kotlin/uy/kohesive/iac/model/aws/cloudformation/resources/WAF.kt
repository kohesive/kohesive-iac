package uy.kohesive.iac.model.aws.cloudformation.resources

import uy.kohesive.iac.model.aws.cloudformation.ResourceProperties
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationType
import uy.kohesive.iac.model.aws.cloudformation.CloudFormationTypes

@CloudFormationTypes
object WAF {

    @CloudFormationType("AWS::WAF::ByteMatchSet")
    data class ByteMatchSet(
        val ByteMatchTuples: List<WAF.ByteMatchSet.ByteMatchTupleProperty>? = null,
        val Name: String
    ) : ResourceProperties {

        data class ByteMatchTupleProperty(
            val FieldToMatch: ByteMatchSet.ByteMatchTupleProperty.FieldToMatchProperty,
            val PositionalConstraint: String,
            val TargetString: String? = null,
            val TargetStringBase64: String? = null,
            val TextTransformation: String
        ) {

            data class FieldToMatchProperty(
                val Data: String? = null,
                val Type: String
            ) 

        }

    }

    @CloudFormationType("AWS::WAF::IPSet")
    data class IPSet(
        val IPSetDescriptors: List<WAF.IPSet.IPSetDescriptorProperty>? = null,
        val Name: String
    ) : ResourceProperties {

        data class IPSetDescriptorProperty(
            val Type: String,
            val Value: String
        ) 

    }

    @CloudFormationType("AWS::WAF::Rule")
    data class Rule(
        val MetricName: String,
        val Name: String,
        val Predicates: List<WAF.Rule.PredicateProperty>? = null
    ) : ResourceProperties {

        data class PredicateProperty(
            val DataId: String,
            val Negated: String,
            val Type: String
        ) 

    }

    @CloudFormationType("AWS::WAF::SizeConstraintSet")
    data class SizeConstraintSet(
        val Name: String,
        val SizeConstraints: List<WAF.SizeConstraintSet.SizeConstraintProperty>
    ) : ResourceProperties {

        data class SizeConstraintProperty(
            val ComparisonOperator: String,
            val FieldToMatch: SizeConstraintSet.SizeConstraintProperty.FieldToMatchProperty,
            val Size: String,
            val TextTransformation: String
        ) {

            data class FieldToMatchProperty(
                val Data: String? = null,
                val Type: String
            ) 

        }

    }

    @CloudFormationType("AWS::WAF::SqlInjectionMatchSet")
    data class SqlInjectionMatchSet(
        val Name: String,
        val SqlInjectionMatchTuples: List<WAF.SqlInjectionMatchSet.SqlInjectionMatchTupleProperty>? = null
    ) : ResourceProperties {

        data class SqlInjectionMatchTupleProperty(
            val FieldToMatch: ByteMatchSet.ByteMatchTupleProperty.FieldToMatchProperty,
            val TextTransformation: String
        ) 

    }

    @CloudFormationType("AWS::WAF::WebACL")
    data class WebACL(
        val DefaultAction: WebACL.ActionProperty,
        val MetricName: String,
        val Name: String,
        val Rules: List<WAF.WebACL.RuleProperty>? = null
    ) : ResourceProperties {

        data class ActionProperty(
            val Type: String
        ) 


        data class RuleProperty(
            val Action: WebACL.ActionProperty,
            val Priority: String,
            val RuleId: String
        ) 

    }

    @CloudFormationType("AWS::WAF::XssMatchSet")
    data class XssMatchSet(
        val Name: String,
        val XssMatchTuples: List<WAF.XssMatchSet.XssMatchTupleProperty>? = null
    ) : ResourceProperties {

        data class XssMatchTupleProperty(
            val FieldToMatch: XssMatchSet.XssMatchTupleProperty.FieldToMatchProperty,
            val TextTransformation: String
        ) {

            data class FieldToMatchProperty(
                val Data: String? = null,
                val Type: String
            ) 

        }

    }


}