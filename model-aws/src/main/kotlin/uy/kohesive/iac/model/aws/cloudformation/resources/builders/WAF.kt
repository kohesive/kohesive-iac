package uy.kohesive.iac.model.aws.cloudformation.resources.builders

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.services.waf.model.*
import uy.kohesive.iac.model.aws.cloudformation.ResourcePropertiesBuilder
import uy.kohesive.iac.model.aws.cloudformation.resources.WAF

class WAFByteMatchSetResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateByteMatchSetRequest> {

    override val requestClazz = CreateByteMatchSetRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateByteMatchSetRequest).let {
            WAF.ByteMatchSet(
                Name = request.name,
                ByteMatchTuples = relatedObjects.filterIsInstance<UpdateByteMatchSetRequest>().flatMap { it.updates }.filter {
                    it.action == ChangeAction.INSERT.name
                }.map {
                    WAF.ByteMatchSet.ByteMatchTupleProperty(
                        FieldToMatch = it.byteMatchTuple.fieldToMatch.let {
                            WAF.ByteMatchSet.ByteMatchTupleProperty.FieldToMatchProperty(
                                Data = it.data,
                                Type = it.data
                            )
                        },
                        PositionalConstraint = it.byteMatchTuple.positionalConstraint,
                        TargetString         = it.byteMatchTuple.targetString?.let { String(it.array()) },
                        TextTransformation   = it.byteMatchTuple.textTransformation
                    )
                }
            )
        }

}

class WAFIPSetResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateIPSetRequest> {

    override val requestClazz = CreateIPSetRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateIPSetRequest).let {
            WAF.IPSet(
                Name = request.name,
                IPSetDescriptors = relatedObjects.filterIsInstance<UpdateIPSetRequest>().flatMap { it.updates }.filter {
                    it.action == ChangeAction.INSERT.name
                }.map {
                    WAF.IPSet.IPSetDescriptorProperty(
                        Type  = it.ipSetDescriptor.type,
                        Value = it.ipSetDescriptor.value
                    )
                }
            )
        }

}

class WAFRuleResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateRuleRequest> {

    override val requestClazz = CreateRuleRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateRuleRequest).let {
            WAF.Rule(
                MetricName = request.metricName,
                Name       = request.name,
                Predicates = relatedObjects.filterIsInstance<UpdateRuleRequest>().flatMap { it.updates }.filter {
                    it.action == ChangeAction.INSERT.name
                }.map {
                    WAF.Rule.PredicateProperty(
                        DataId  = it.predicate.dataId,
                        Type    = it.predicate.type,
                        Negated = it.predicate.negated.toString()
                    )
                }
            )
        }

}

class WAFSizeConstraintSetResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateSizeConstraintSetRequest> {

    override val requestClazz = CreateSizeConstraintSetRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateSizeConstraintSetRequest).let {
            WAF.SizeConstraintSet(
                Name            = request.name,
                SizeConstraints = relatedObjects.filterIsInstance<UpdateSizeConstraintSetRequest>().flatMap { it.updates }.filter {
                    it.action == ChangeAction.INSERT.name
                }.map {
                    WAF.SizeConstraintSet.SizeConstraintProperty(
                        ComparisonOperator = it.sizeConstraint.comparisonOperator,
                        TextTransformation = it.sizeConstraint.textTransformation,
                        FieldToMatch       = it.sizeConstraint.fieldToMatch.let {
                            WAF.SizeConstraintSet.SizeConstraintProperty.FieldToMatchProperty(
                                Data = it.data,
                                Type = it.type
                            )
                        },
                        Size = it.sizeConstraint.size.toString()
                    )
                }
            )
        }

}

class WAFSqlInjectionMatchSetResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateSqlInjectionMatchSetRequest> {

    override val requestClazz = CreateSqlInjectionMatchSetRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateSqlInjectionMatchSetRequest).let {
            WAF.SqlInjectionMatchSet(
                Name = request.name,
                SqlInjectionMatchTuples = relatedObjects.filterIsInstance<UpdateSqlInjectionMatchSetRequest>().flatMap { it.updates }.filter {
                    it.action == ChangeAction.INSERT.name
                }.map {
                    WAF.SqlInjectionMatchSet.SqlInjectionMatchTupleProperty(
                        FieldToMatch = it.sqlInjectionMatchTuple.fieldToMatch.let {
                            WAF.ByteMatchSet.ByteMatchTupleProperty.FieldToMatchProperty(
                                Data = it.data,
                                Type = it.type
                            )
                        },
                        TextTransformation = it.sqlInjectionMatchTuple.textTransformation
                    )
                }
            )
        }

}

class WAFWebACLResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateWebACLRequest> {

    override val requestClazz = CreateWebACLRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateWebACLRequest).let {
            WAF.WebACL(
                DefaultAction = request.defaultAction.let {
                    WAF.WebACL.ActionProperty(
                        Type = it.type
                    )
                },
                MetricName = request.metricName,
                Name       = request.name,
                Rules      = relatedObjects.filterIsInstance<UpdateWebACLRequest>().flatMap { it.updates }.filter {
                    it.action == ChangeAction.INSERT.name
                }.map {
                    WAF.WebACL.RuleProperty(
                        Action = it.action.let {
                            WAF.WebACL.ActionProperty(
                                Type = it
                            )
                        },
                        Priority = it.activatedRule.priority.toString(),
                        RuleId   = it.activatedRule.ruleId
                    )
                }
            )
        }

}

class WAFXssMatchSetResourcePropertiesBuilder : ResourcePropertiesBuilder<CreateXssMatchSetRequest> {

    override val requestClazz = CreateXssMatchSetRequest::class

    override fun buildResource(request: AmazonWebServiceRequest, relatedObjects: List<Any>) =
        (request as CreateXssMatchSetRequest).let {
            WAF.XssMatchSet(
                Name           = request.name,
                XssMatchTuples = relatedObjects.filterIsInstance<UpdateXssMatchSetRequest>().flatMap { it.updates }.filter {
                    it.action == ChangeAction.INSERT.name
                }.map {
                    WAF.XssMatchSet.XssMatchTupleProperty(
                        FieldToMatch = it.xssMatchTuple.fieldToMatch.let {
                            WAF.XssMatchSet.XssMatchTupleProperty.FieldToMatchProperty(
                                Data = it.data,
                                Type = it.type
                            )
                        },
                        TextTransformation = it.xssMatchTuple.textTransformation
                    )
                }
            )
        }

}

