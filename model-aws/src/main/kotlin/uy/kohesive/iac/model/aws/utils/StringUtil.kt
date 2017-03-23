package uy.kohesive.iac.model.aws.utils

fun String.firstLetterToLowerCase() = take(1).toLowerCase() + drop(1)
fun String.firstLetterToUpperCase() = take(1).toUpperCase() + drop(1)