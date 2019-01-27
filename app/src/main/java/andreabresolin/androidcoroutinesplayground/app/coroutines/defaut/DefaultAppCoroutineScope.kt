package andreabresolin.androidcoroutinesplayground.app.coroutines.defaut

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutineScope
import andreabresolin.androidcoroutinesplayground.app.coroutines.CoroutineDispatcherProvider
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DefaultAppCoroutineScope
@Inject constructor(private val coroutineDispatcherProvider: CoroutineDispatcherProvider) : AppCoroutineScope {

    private val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = coroutineDispatcherProvider.main + job

    override fun uiTask(block: suspend CoroutineScope.() -> Unit) {
        launch(coroutineDispatcherProvider.main) { block() }
    }

    override suspend fun <T> backgroundTask(block: suspend CoroutineScope.() -> T): T {
        return withContext(coroutineDispatcherProvider.background) { block() }
    }

    override suspend fun <T> ioTask(block: suspend CoroutineScope.() -> T): T {
        return withContext(coroutineDispatcherProvider.io) { block() }
    }

    override fun <T> parallelTask(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return async(coroutineDispatcherProvider.background) { block() }
    }

    override fun <T> parallelIOTask(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return async(coroutineDispatcherProvider.io) { block() }
    }

    override suspend fun delayTask(milliseconds: Long) {
        delay(milliseconds)
    }

    override fun cancelAllTasks() = job.cancelChildren()
}