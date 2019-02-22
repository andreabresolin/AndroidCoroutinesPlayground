package andreabresolin.androidcoroutinesplayground.app.coroutines

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutinesHelpers.Companion.startJob
import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutinesHelpers.Companion.startTask
import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutinesHelpers.Companion.startTaskAsync
import kotlinx.coroutines.*

fun CoroutineScope.uiJob(timeout: Long = 0L, block: suspend CoroutineScope.() -> Unit) {
    startJob(this, AppCoroutinesConfiguration.uiDispatcher, timeout, block)
}

fun CoroutineScope.backgroundJob(timeout: Long = 0L, block: suspend CoroutineScope.() -> Unit) {
    startJob(this, AppCoroutinesConfiguration.backgroundDispatcher, timeout, block)
}

fun CoroutineScope.ioJob(timeout: Long = 0L, block: suspend CoroutineScope.() -> Unit) {
    startJob(this, AppCoroutinesConfiguration.ioDispatcher, timeout, block)
}

suspend fun <T> uiTask(timeout: Long = 0L, block: suspend CoroutineScope.() -> T): T {
    return startTask(AppCoroutinesConfiguration.uiDispatcher, timeout, block)
}

suspend fun <T> backgroundTask(timeout: Long = 0L, block: suspend CoroutineScope.() -> T): T {
    return startTask(AppCoroutinesConfiguration.backgroundDispatcher, timeout, block)
}

suspend fun <T> ioTask(timeout: Long = 0L, block: suspend CoroutineScope.() -> T): T {
    return startTask(AppCoroutinesConfiguration.ioDispatcher, timeout, block)
}

fun <T> CoroutineScope.uiTaskAsync(timeout: Long = 0L, block: suspend CoroutineScope.() -> T): Deferred<T> {
    return startTaskAsync(this, AppCoroutinesConfiguration.uiDispatcher, timeout, block)
}

fun <T> CoroutineScope.backgroundTaskAsync(timeout: Long = 0L, block: suspend CoroutineScope.() -> T): Deferred<T> {
    return startTaskAsync(this, AppCoroutinesConfiguration.backgroundDispatcher, timeout, block)
}

fun <T> CoroutineScope.ioTaskAsync(timeout: Long = 0L, block: suspend CoroutineScope.() -> T): Deferred<T> {
    return startTaskAsync(this, AppCoroutinesConfiguration.ioDispatcher, timeout, block)
}

suspend fun delayTask(milliseconds: Long) {
    if (AppCoroutinesConfiguration.isDelayEnabled) {
        delay(milliseconds)
    }
}

suspend fun <T> Deferred<T>.awaitOrReturn(returnIfCancelled: T): T {
    return try {
        await()
    } catch (e: CancellationException) {
        returnIfCancelled
    }
}

suspend fun <T> awaitAllOrCancel(vararg deferreds: Deferred<T>): List<T> {
    try {
        return awaitAll(*deferreds)
    } catch (e: Exception) {
        if (e !is CancellationException) {
            deferreds.forEach { if (it.isActive) it.cancel() }
        }

        throw e
    }
}