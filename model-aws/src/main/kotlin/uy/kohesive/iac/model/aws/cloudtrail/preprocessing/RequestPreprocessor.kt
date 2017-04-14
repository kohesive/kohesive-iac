package uy.kohesive.iac.model.aws.cloudtrail.preprocessing

import uy.kohesive.iac.model.aws.cloudtrail.RequestMap

interface RequestPreprocessor {

    val eventNames: List<String>

    fun process(requestMap: RequestMap): RequestMap

}

object RequestPreprocessors {

    private val preprocessors: List<RequestPreprocessor> = listOf(
        RunInstancesPreprocessor(),
        CreateNetworkInterfacePreprocessor(),
        CreateBucketPreprocessor()
    )

    private val eventNameToPreProcessors = preprocessors.flatMap { preprocessor ->
        preprocessor.eventNames.map { eventName ->
            eventName to preprocessor
        }
    }.groupBy { it.first }.mapValues { it.value.map { it.second } }

    fun preprocess(eventName: String, requestMap: RequestMap): RequestMap =
        eventNameToPreProcessors[eventName]?.fold(requestMap) { currentRequest, preprocessor ->
            preprocessor.process(currentRequest)
        } ?: requestMap

}