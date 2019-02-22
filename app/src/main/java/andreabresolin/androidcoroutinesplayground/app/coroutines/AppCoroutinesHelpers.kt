package andreabresolin.androidcoroutinesplayground.app.coroutines

import andreabresolin.androidcoroutinesplayground.app.util.logCompleted
import andreabresolin.androidcoroutinesplayground.app.util.logStarted
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.coroutines.CoroutineContext

internal class AppCoroutinesHelpers {

    companion object {
        private enum class HelperType {
            JOB, TASK, TASK_ASYNC
        }

        private val loggingTaskId = AtomicLong(1L)

        private suspend fun <T> executeBlock(
            parentScope: CoroutineScope,
            callerType: HelperType,
            coroutineContext: CoroutineContext,
            block: suspend CoroutineScope.() -> T
        ): T {
            if (AppCoroutinesConfiguration.isLoggingEnabled) {
                val taskId = loggingTaskId.getAndIncrement()

                val methodName = when (coroutineContext) {
                    AppCoroutinesConfiguration.uiDispatcher -> "ui"
                    AppCoroutinesConfiguration.backgroundDispatcher -> "background"
                    AppCoroutinesConfiguration.ioDispatcher -> "io"
                    else -> ""
                } + when (callerType) {
                    HelperType.JOB -> "Job"
                    HelperType.TASK -> "Task"
                    HelperType.TASK_ASYNC -> "TaskAsync"
                } + "#$taskId"

                logStarted(methodName)
                val result = parentScope.block()
                logCompleted(methodName)
                return result
            } else {
                return parentScope.block()
            }
        }

        private fun computeTimeout(timeout: Long): Long {
            return if (AppCoroutinesConfiguration.useTestTimeout) {
                AppCoroutinesConfiguration.TEST_TIMEOUT
            } else {
                timeout
            }
        }

        fun startJob(
            parentScope: CoroutineScope,
            coroutineContext: CoroutineContext,
            timeout: Long = 0L,
            block: suspend CoroutineScope.() -> Unit
        ) {
            parentScope.launch(coroutineContext) {
                supervisorScope {
                    if (timeout > 0L) {
                        withTimeout(computeTimeout(timeout)) {
                            executeBlock(this, HelperType.JOB, coroutineContext, block)
                        }
                    } else {
                        executeBlock(this, HelperType.JOB, coroutineContext, block)
                    }
                }
            }
        }

        suspend fun <T> startTask(
            coroutineContext: CoroutineContext,
            timeout: Long = 0L,
            block: suspend CoroutineScope.() -> T
        ): T {
            return withContext(coroutineContext) {
                return@withContext if (timeout > 0L) {
                    withTimeout(computeTimeout(timeout)) {
                        executeBlock(this, HelperType.TASK, coroutineContext, block)
                    }
                } else {
                    executeBlock(this, HelperType.TASK, coroutineContext, block)
                }
            }
        }

        fun <T> startTaskAsync(
            parentScope: CoroutineScope,
            coroutineContext: CoroutineContext,
            timeout: Long = 0L,
            block: suspend CoroutineScope.() -> T
        ): Deferred<T> {
            return parentScope.async(coroutineContext) {
                return@async supervisorScope {
                    return@supervisorScope if (timeout > 0L) {
                        withTimeout(computeTimeout(timeout)) {
                            executeBlock(this, HelperType.TASK_ASYNC, coroutineContext, block)
                        }
                    } else {
                        executeBlock(this, HelperType.TASK_ASYNC, coroutineContext, block)
                    }
                }
            }
        }
    }
}
