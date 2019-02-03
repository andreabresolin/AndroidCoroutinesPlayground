package andreabresolin.androidcoroutinesplayground.app.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred

interface AppCoroutineScope : CoroutineScope {

    fun uiJob(block: suspend CoroutineScope.() -> Unit)

    fun backgroundJob(block: suspend CoroutineScope.() -> Unit)

    fun ioJob(block: suspend CoroutineScope.() -> Unit)

    suspend fun <T> uiTask(block: suspend CoroutineScope.() -> T): T

    suspend fun <T> backgroundTask(block: suspend CoroutineScope.() -> T): T

    suspend fun <T> ioTask(block: suspend CoroutineScope.() -> T): T

    fun <T> uiTaskAsync(block: suspend CoroutineScope.() -> T): Deferred<T>

    fun <T> backgroundTaskAsync(block: suspend CoroutineScope.() -> T): Deferred<T>

    fun <T> ioTaskAsync(block: suspend CoroutineScope.() -> T): Deferred<T>

    suspend fun delayTask(milliseconds: Long)

    fun cancelAll()
}