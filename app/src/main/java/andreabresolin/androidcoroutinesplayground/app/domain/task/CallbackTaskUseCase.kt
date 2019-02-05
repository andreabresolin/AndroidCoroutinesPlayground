package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.coroutines.backgroundTask
import andreabresolin.androidcoroutinesplayground.app.coroutines.delayTask
import andreabresolin.androidcoroutinesplayground.app.domain.BaseUseCase
import andreabresolin.androidcoroutinesplayground.app.exception.CustomTaskException
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionError
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random

class CallbackTaskUseCase
@Inject constructor() : BaseUseCase() {

    private class ExecutorWithCallback {

        fun executeAction(
            input: String,
            successCallback: (Int) -> Unit,
            errorCallback: () -> Unit
        ) {
            if (input == "SUCCESS") {
                successCallback(10)
            } else {
                errorCallback()
            }
        }
    }

    suspend fun execute(param: String): TaskExecutionResult = backgroundTask {
        val taskDuration = Random.nextLong(1000, 2000)
        delayTask(taskDuration)

        return@backgroundTask suspendCoroutine<TaskExecutionResult> { continuation ->
            ExecutorWithCallback().executeAction(param,
                { result -> successCallback(result, continuation) },
                { errorCallback(continuation) })
        }
    }

    private fun successCallback(result: Int, continuation: Continuation<TaskExecutionResult>) {
        continuation.resume(TaskExecutionSuccess(result.toLong()))
    }

    private fun errorCallback(continuation: Continuation<TaskExecutionResult>) {
        continuation.resume(TaskExecutionError(CustomTaskException()))
    }
}