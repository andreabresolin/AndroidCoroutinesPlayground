package andreabresolin.androidcoroutinesplayground.app.util

import kotlinx.coroutines.CoroutineScope
import timber.log.Timber

fun CoroutineScope.logStarted(methodName: String) {
    Timber.d("[%s][%s] Started", methodName, Thread.currentThread().name)
}

fun CoroutineScope.logCompleted(methodName: String) {
    Timber.d("[%s][%s] Completed", methodName, Thread.currentThread().name)
}

fun CoroutineScope.logCancelled(methodName: String) {
    Timber.d("[%s][%s] Cancelled", methodName, Thread.currentThread().name)
}

fun CoroutineScope.logIteration(methodName: String, iterationNumber: Long) {
    Timber.d("[%s][%s] Iteration %s", methodName, Thread.currentThread().name, iterationNumber.toString())
}