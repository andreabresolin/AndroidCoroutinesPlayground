package andreabresolin.androidcoroutinesplayground.app.coroutines.testing

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

    override fun uiJob(block: suspend CoroutineScope.() -> Unit) {
        startJob(coroutineDispatcherProvider.main, block)
    }

    override fun backgroundJob(block: suspend CoroutineScope.() -> Unit) {
        startJob(coroutineDispatcherProvider.background, block)
    }

    override fun ioJob(block: suspend CoroutineScope.() -> Unit) {
        startJob(coroutineDispatcherProvider.io, block)
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
        // Do nothing
    }

    override fun cancelAll() = job.cancelChildren()

    private fun startJob(coroutineContext: CoroutineContext,
                         block: suspend CoroutineScope.() -> Unit) {
        launch(coroutineContext) { block() }
    }

    private suspend fun <T> startTask(coroutineContext: CoroutineContext,
                                      block: suspend CoroutineScope.() -> T): T {
        return withContext(coroutineContext) { block() }
    }

    private fun <T> startTaskAsync(coroutineContext: CoroutineContext,
                                   block: suspend CoroutineScope.() -> T): Deferred<T> {
        return async(coroutineContext) { block() }
    }
}