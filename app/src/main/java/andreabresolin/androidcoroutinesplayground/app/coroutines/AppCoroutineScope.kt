package andreabresolin.androidcoroutinesplayground.app.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred

interface AppCoroutineScope : CoroutineScope {

    fun uiTask(block: suspend CoroutineScope.() -> Unit)

    suspend fun <T> backgroundTask(block: suspend CoroutineScope.() -> T): T

    suspend fun <T> ioTask(block: suspend CoroutineScope.() -> T): T

    fun <T> parallelTask(block: suspend CoroutineScope.() -> T): Deferred<T>

    fun <T> parallelIOTask(block: suspend CoroutineScope.() -> T): Deferred<T>

    suspend fun delayTask(milliseconds: Long)

    fun cancelAllTasks()
}