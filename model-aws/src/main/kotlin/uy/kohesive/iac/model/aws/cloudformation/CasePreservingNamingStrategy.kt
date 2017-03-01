package uy.kohesive.iac.model.aws.cloudformation

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.cfg.MapperConfig
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod

class CasePreservingNamingStrategy : PropertyNamingStrategy() {
    override fun nameForGetterMethod(config: MapperConfig<*>, method: AnnotatedMethod, defaultName: String)
        = convert(method.name)
    override fun nameForSetterMethod(config: MapperConfig<*>, method: AnnotatedMethod, defaultName: String)
        = convert(method.name)
    private fun convert(input: String) = input.substring(3)
}
