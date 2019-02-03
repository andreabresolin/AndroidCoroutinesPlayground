package andreabresolin.androidcoroutinesplayground.app.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Deferred

suspend fun <T> Deferred<T>.awaitOrReturn(returnIfCancelled: T): T {
    return try {
        await()
    } catch (e: CancellationException) {
        returnIfCancelled
    }
}
