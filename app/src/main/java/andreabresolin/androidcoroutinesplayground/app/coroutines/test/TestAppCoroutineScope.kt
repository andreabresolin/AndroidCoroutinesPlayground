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

    override fun uiCoroutine(block: suspend CoroutineScope.() -> Unit) = startCoroutine(block)

    override fun backgroundCoroutine(block: suspend CoroutineScope.() -> Unit) = startCoroutine(block)

    override fun ioCoroutine(block: suspend CoroutineScope.() -> Unit) = startCoroutine(block)

    override suspend fun <T> uiTask(block: suspend CoroutineScope.() -> T): T  = startTask(block)

    override suspend fun <T> backgroundTask(block: suspend CoroutineScope.() -> T): T = startTask(block)

    override suspend fun <T> ioTask(block: suspend CoroutineScope.() -> T): T = startTask(block)

    override fun <T> uiTaskAsync(block: suspend CoroutineScope.() -> T): Deferred<T> = startTaskAsync(block)

    override fun <T> backgroundTaskAsync(block: suspend CoroutineScope.() -> T): Deferred<T> = startTaskAsync(block)

    override fun <T> ioTaskAsync(block: suspend CoroutineScope.() -> T): Deferred<T> = startTaskAsync(block)

    override suspend fun delayTask(milliseconds: Long) {
        // Do nothing
    }

    override fun cancelAllTasks() = job.cancelChildren()

    private fun startCoroutine(block: suspend CoroutineScope.() -> Unit) {
        runBlocking { block() }
    }

    private suspend fun <T> startTask(block: suspend CoroutineScope.() -> T): T {
        return runBlocking { block() }
    }

    private fun <T> startTaskAsync(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return CompletableDeferred(runBlocking { block() })
    }
}