package andreabresolin.androidcoroutinesplayground.app.coroutines.defaut

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutineScope
import andreabresolin.androidcoroutinesplayground.app.coroutines.CoroutineDispatcherProvider
import andreabresolin.androidcoroutinesplayground.app.util.logCompleted
import andreabresolin.androidcoroutinesplayground.app.util.logStarted
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LoggingAppCoroutineScope
@Inject constructor(private val coroutineDispatcherProvider: CoroutineDispatcherProvider) : AppCoroutineScope {

    private val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = coroutineDispatcherProvider.main + job

    override fun uiCoroutine(block: suspend CoroutineScope.() -> Unit) {
        startCoroutine(coroutineDispatcherProvider.main, block)
    }

    override fun backgroundCoroutine(block: suspend CoroutineScope.() -> Unit) {
        startCoroutine(coroutineDispatcherProvider.background, block)
    }

    override fun ioCoroutine(block: suspend CoroutineScope.() -> Unit) {
        startCoroutine(coroutineDispatcherProvider.io, block)
    }

    override suspend fun <T> uiTask(block: suspend CoroutineScope.() -> T): T {
        return startTask(coroutineDispatcherProvider.main, block)
    }

    override suspend fun <T> backgroundTask(block: suspend CoroutineScope.() -> T): T {
        return startTask(coroutineDispatcherProvider.background, block)
    }

    override suspend fun <T> ioTask(block: suspend CoroutineScope.() -> T): T {
        return startTask(coroutineDispatcherProvider.io, block)
    }

    override fun <T> uiTaskAsync(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return startTaskAsync(coroutineDispatcherProvider.main, block)
    }

    override fun <T> backgroundTaskAsync(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return startTaskAsync(coroutineDispatcherProvider.background, block)
    }

    override fun <T> ioTaskAsync(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return startTaskAsync(coroutineDispatcherProvider.io, block)
    }

    override suspend fun delayTask(milliseconds: Long) {
        delay(milliseconds)
    }

    override fun cancelAllTasks() = job.cancelChildren()

    private fun getCoroutineContextPrefix(coroutineContext: CoroutineContext): String {
        return when (coroutineContext) {
            coroutineDispatcherProvider.main -> "ui"
            coroutineDispatcherProvider.background -> "background"
            coroutineDispatcherProvider.io -> "io"
            else -> ""
        }
    }

    private fun startCoroutine(coroutineContext: CoroutineContext,
                               block: suspend CoroutineScope.() -> Unit) {
        launch(coroutineContext) {
            val methodName = getCoroutineContextPrefix(coroutineContext) + "Coroutine"
            logStarted(methodName)
            block()
            logCompleted(methodName)
        }
    }

    private suspend fun <T> startTask(coroutineContext: CoroutineContext,
                                      block: suspend CoroutineScope.() -> T): T {
        return withContext(coroutineContext) {
            val methodName = getCoroutineContextPrefix(coroutineContext) + "Task"
            logStarted(methodName)
            val result = block()
            logCompleted(methodName)
            return@withContext result
        }
    }

    private fun <T> startTaskAsync(coroutineContext: CoroutineContext,
                                   block: suspend CoroutineScope.() -> T): Deferred<T> {
        return async(coroutineContext) {
            val methodName = getCoroutineContextPrefix(coroutineContext) + "TaskAsync"
            logStarted(methodName)
            val result = block()
            logCompleted(methodName)
            return@async result
        }
    }
}