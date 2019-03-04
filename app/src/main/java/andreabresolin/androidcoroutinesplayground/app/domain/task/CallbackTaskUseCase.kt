package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.coroutines.backgroundTask
import andreabresolin.androidcoroutinesplayground.app.coroutines.delayTask
import andreabresolin.androidcoroutinesplayground.app.domain.BaseUseCase
import andreabresolin.androidcoroutinesplayground.app.exception.CustomTaskException
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.random.Random

class CallbackTaskUseCase
@Inject constructor() : BaseUseCase() {

    private class ExecutorWithCallback {

        fun executeAction(
            input: String,
            successCallback: (Long) -> Unit,
            cancelCallback: () -> Unit,
            errorCallback: () -> Unit
        ) {
            when (input) {
                "SUCCESS" -> successCallback(10L)
                "CANCEL" -> cancelCallback()
                else -> errorCallback()
            }
        }
    }

    suspend fun execute(param: String): TaskExecutionResult = backgroundTask {
        val taskDuration = Random.nextLong(1000, 2000)
        delayTask(taskDuration)

        return@backgroundTask suspendCancellableCoroutine<TaskExecutionResult> { continuation ->
            ExecutorWithCallback().executeAction(param,
                { result -> successCallback(result, continuation) },
                { cancelCallback(continuation) },
                { errorCallback(continuation) })
        }
    }

    private fun successCallback(result: Long, continuation: CancellableContinuation<TaskExecutionResult>) {
        continuation.resume(TaskExecutionSuccess(result))
    }

    private fun cancelCallback(continuation: CancellableContinuation<TaskExecutionResult>) {
        continuation.cancel()
    }

    private fun errorCallback(continuation: CancellableContinuation<TaskExecutionResult>) {
        continuation.resumeWithException(CustomTaskException())
    }
}