package uy.kohesive.iac.examples.aws.lambdas

fun String.ensureSingleLine(): String = this.replace('\n', ' ')