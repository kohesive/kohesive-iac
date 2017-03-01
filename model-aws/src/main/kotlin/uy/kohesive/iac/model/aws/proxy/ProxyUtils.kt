package uy.kohesive.iac.model.aws.proxy

import net.sf.cglib.proxy.Enhancer
import net.sf.cglib.proxy.MethodInterceptor
import uy.kohesive.iac.model.aws.IacContext
import java.util.*
import kotlin.reflect.KFunction1

internal object ProxyUtils {
    val INCLUDE_ALL_PROPS: List<KFunction1<Any, Any>> = ArrayList()
}

internal inline fun <S, reified T : Any> makeListProxy(
    context: IacContext,
    baseName: String,
    requestObjects: List<S>,
    copyFromReq: Map<KFunction1<S, Any>, KFunction1<T, Any>>,
    includeReferences: List<KFunction1<T, Any>>  = ProxyUtils.INCLUDE_ALL_PROPS,
    disallowReferences: List<KFunction1<T, Any>> = copyFromReq.values.toList()
): List<T> {
    return requestObjects.mapIndexed { idx, obj ->
        makeProxy(context, "$baseName[$idx]", obj, copyFromReq, includeReferences, disallowReferences)
    }
}

internal inline fun <S, reified T : Any> makeProxy(
    context: IacContext,
    sourceName: String,
    requestObject: S,
    copyFromReq: Map<KFunction1<S, Any>, KFunction1<T, Any>> = emptyMap(),
    includeReferences: List<KFunction1<T, Any>> = ProxyUtils.INCLUDE_ALL_PROPS,
    disallowReferences: List<KFunction1<T, Any>> = copyFromReq.values.toList()
): T {
    return with (context) {
        val delegate = T::class.java.newInstance()
        (Enhancer.create(T::class.java, MethodInterceptor { _, method, args, _ ->
            copyFromReq.keys.firstOrNull { it.name == method.name }?.let { sourceFunction ->
                return@MethodInterceptor sourceFunction.invoke(requestObject)
            }
            if (method.name.startsWith("get") && method.returnType == String::class.java) {
                if (disallowReferences.any { it.name == method.name }) {
                    throw IllegalArgumentException("${method.name} is disallowed for referencing")
                } else {
                    if (includeReferences === ProxyUtils.INCLUDE_ALL_PROPS || includeReferences.any { it.name == method.name }) {
                        return@MethodInterceptor createReference<T>(sourceName, method.name.drop(3))
                    } else {
                        throw IllegalArgumentException("${method.name} is disallowed for referencing")
                    }
                }
            } else {
                return@MethodInterceptor method.invoke(delegate, * args)
            }
        }) as T).registerWithName(sourceName)
    }
}