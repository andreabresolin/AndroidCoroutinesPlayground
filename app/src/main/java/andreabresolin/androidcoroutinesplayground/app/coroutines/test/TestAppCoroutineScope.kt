package andreabresolin.androidcoroutinesplayground.app.coroutines.test

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutineScope
import andreabresolin.androidcoroutinesplayground.app.coroutines.CoroutineDispatcherProvider
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TestAppCoroutineScope
@Inject constructor(private val coroutineDispatcherProvider: CoroutineDispatcherProvider) : AppCoroutineScope {

    private val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = coroutineDispatcherProvider.main + job

    override fun uiTask(block: suspend CoroutineScope.() -> Unit) = runBlocking { block() }

    override suspend fun <T> backgroundTask(block: suspend CoroutineScope.() -> T): T = runBlocking { block() }

    override suspend fun <T> ioTask(block: suspend CoroutineScope.() -> T): T = runBlocking { block() }

    override fun <T> parallelTask(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return CompletableDeferred(runBlocking { block() })
    }

    override fun <T> parallelIOTask(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return CompletableDeferred(runBlocking { block() })
    }

    override suspend fun delayTask(milliseconds: Long) {
        // Do nothing
    }

    override fun cancelAllTasks() = job.cancelChildren()
}