package uy.kohesive.iac.model.aws.cloudtrail.preprocessing

import uy.kohesive.iac.model.aws.cloudtrail.CloudTrailEvent

interface CloudTrailEventPreprocessor {

    val eventNames: List<String>

    fun preprocess(event: CloudTrailEvent): CloudTrailEvent

}

object CloudTrailEventPreprocessors {

    private val preprocessors: List<CloudTrailEventPreprocessor> = listOf(
        RunInstancesPreprocessor(),
        CreateNetworkInterfacePreprocessor(),
        CreateBucketPreprocessor(),
        PutBucketPolicyPreprocessor(),
        PutBucketAclPreprocessor(),
        PutBucketLoggingPreprocessor()
    )

    private val eventNameToPreProcessors = preprocessors.flatMap { preprocessor ->
        preprocessor.eventNames.map { eventName ->
            eventName to preprocessor
        }
    }.groupBy { it.first }.mapValues { it.value.map { it.second } }

    fun preprocess(event: CloudTrailEvent): CloudTrailEvent =
        eventNameToPreProcessors[event.eventName]?.fold(event) { currentEvent, preprocessor ->
            preprocessor.preprocess(currentEvent)
        } ?: event

}