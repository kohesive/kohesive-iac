package uy.kohesive.iac.model.aws.proxy

import com.amazonaws.services.ec2.model.Instance
import com.amazonaws.services.ec2.model.RunInstancesRequest
import net.sf.cglib.proxy.Enhancer
import net.sf.cglib.proxy.MethodInterceptor
import java.util.*
import kotlin.reflect.KFunction1

fun main(args: Array<String>) {
    val request = RunInstancesRequest().withImageId("someImageId")

    val instance = makeProxy<RunInstancesRequest, Instance>(
        id          = "someId",
        sourceObj   = request,
        copyFromReq = mapOf(
            RunInstancesRequest::getImageId to Instance::getImageId
        )
    )

    println(instance.imageId)
    println(instance.subnetId)
}

object ProxyUtils {
    val INCLUDE_ALL_PROPS: List<KFunction1<Any, Any>> = ArrayList()
}

fun createReference(target: String, property: String) = "{{kohesive:ref:$target:$property}}"

inline fun <S, reified T : Any> makeProxy(
    id: String,
    sourceObj: S,
    copyFromReq: Map<KFunction1<S, Any>, KFunction1<T, Any>>,
    includeReferences: List<KFunction1<T, Any>>  = ProxyUtils.INCLUDE_ALL_PROPS,
    disallowReferences: List<KFunction1<T, Any>> = copyFromReq.values.toList()
): T {
    return Enhancer.create(T::class.java, MethodInterceptor { obj, method, args, proxy ->
        copyFromReq.keys.firstOrNull { it.name == method.name }?.let { sourceFunction ->
            return@MethodInterceptor sourceFunction.invoke(sourceObj)
        }
        if (method.returnType != String::class.java) {
            throw IllegalArgumentException("Expecting referenced String type")
        }
        if (method.name.startsWith("get")) {
            if (disallowReferences.any { it.name == method.name }) {
                throw IllegalArgumentException("${ method.name } is disallowed for referencing")
            } else {
                if (includeReferences === ProxyUtils.INCLUDE_ALL_PROPS || includeReferences.any { it.name == method.name }) {
                    val propertyName = method.name.drop(3).let {
                        it[0] + it.substring(1)
                    }
                    return@MethodInterceptor createReference(id, propertyName)
                } else {
                    throw IllegalArgumentException("${ method.name } is disallowed for referencing")
                }
            }
        } else {
            throw IllegalArgumentException("Expecting a getter")
        }
    }) as T
}