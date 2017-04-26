package uy.kohesive.iac.model.aws.cloudtrail

interface EventsProcessor {

    fun <T> process(processor: (CloudTrailEvent) -> T): Sequence<T>

}