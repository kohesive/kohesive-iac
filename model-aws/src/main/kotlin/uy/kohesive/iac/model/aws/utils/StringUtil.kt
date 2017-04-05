package uy.kohesive.iac.model.aws.utils

fun String.firstLetterToLowerCase() = take(1).toLowerCase() + drop(1)
fun String.firstLetterToUpperCase() = take(1).toUpperCase() + drop(1)

fun String.singularize(): String = Inflector.getInstance().singularize(this)

fun String.namespace(): String = substring(0, lastIndexOf('.'))
fun String.simpleName(): String = substring(lastIndexOf('.') + 1)