package andreabresolin.androidcoroutinesplayground.app.coroutines.defaut

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutineScope
import andreabresolin.androidcoroutinesplayground.app.coroutines.CoroutineDispatcherProvider
import andreabresolin.androidcoroutinesplayground.app.util.logTaskCompleted
import andreabresolin.androidcoroutinesplayground.app.util.logTaskStarted
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LoggingAppCoroutineScope
@Inject constructor(private val coroutineDispatcherProvider: CoroutineDispatcherProvider) : AppCoroutineScope {

    private val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = coroutineDispatcherProvider.main + job

    override fun uiTask(block: suspend CoroutineScope.() -> Unit) {
        launch(coroutineDispatcherProvider.main) {
            logTaskStarted("uiTask")
            block()
            logTaskCompleted("uiTask")
        }
    }

    override suspend fun <T> backgroundTask(block: suspend CoroutineScope.() -> T): T {
        return withContext(coroutineDispatcherProvider.background) {
            logTaskStarted("backgroundTask")
            val result = block()
            logTaskCompleted("backgroundTask")
            return@withContext result
        }
    }

    override suspend fun <T> ioTask(block: suspend CoroutineScope.() -> T): T {
        return withContext(coroutineDispatcherProvider.io) {
            logTaskStarted("ioTask")
            val result = block()
            logTaskCompleted("ioTask")
            return@withContext result
        }
    }

    override fun <T> parallelTask(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return async(coroutineDispatcherProvider.background) {
            logTaskStarted("parallelTask")
            val result = block()
            logTaskCompleted("parallelTask")
            return@async result
        }
    }

    override fun <T> parallelIOTask(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return async(coroutineDispatcherProvider.io) {
            logTaskStarted("parallelIOTask")
            val result = block()
            logTaskCompleted("parallelIOTask")
            return@async result
        }
    }

    override suspend fun delayTask(milliseconds: Long) {
        delay(milliseconds)
    }

    override fun cancelAllTasks() = job.cancelChildren()
}