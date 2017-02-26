package uy.kohesive.iac.model.aws.utils

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

fun <I: Any, T: Any, WRAPPED: Any, IR> writeOnlyVirtualProperty(setFunc: I.(WRAPPED)->IR, converter: (T)->WRAPPED): VirtualWriteOnlyProperty<I, T, WRAPPED, IR> = VirtualWriteOnlyProperty(setFunc, converter)

class VirtualWriteOnlyProperty<I: Any, T: Any, WRAPPED: Any, IR>(val setFunc: I.(WRAPPED)->IR, val converter: (T)->WRAPPED) : ReadWriteProperty<I, T> {
    override fun getValue(thisRef: I, property: KProperty<*>): T {
        throw IllegalStateException("Property ${property.name} is write-only")
    }

    override fun setValue(thisRef: I, property: KProperty<*>, value: T) {
        thisRef.setFunc(converter(value))
    }
}
