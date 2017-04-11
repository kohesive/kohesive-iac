package uy.kohesive.iac.model.aws.cloudtrail.preprocessing

import uy.kohesive.iac.model.aws.cloudtrail.RequestMap

interface RequestPreprocessor {

    val eventNames: List<String>

    fun process(requestMap: RequestMap): RequestMap

}

object RequestPreprocessors {

    private val preprocessors: List<RequestPreprocessor> = listOf(
        RunInstancesPreprocessor()
    )

    private val eventNameToPreProcessors = preprocessors.flatMap { preprocessor ->
        preprocessor.eventNames.map { eventName ->
            eventName to preprocessors
        }
    }.groupBy { it.first }.mapValues { it.value.flatMap { it.second } }

    fun preprocess(eventName: String, requestMap: RequestMap): RequestMap =
        eventNameToPreProcessors[eventName]?.let { preprocessors ->
            preprocessors.fold(requestMap) { currentRequest, preprocessor ->
                preprocessor.process(currentRequest)
            }
        } ?: requestMap

}