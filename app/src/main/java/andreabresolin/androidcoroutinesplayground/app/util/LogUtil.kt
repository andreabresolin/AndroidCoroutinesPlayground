package andreabresolin.androidcoroutinesplayground.app.util

import kotlinx.coroutines.CoroutineScope
import timber.log.Timber

fun CoroutineScope.logTaskStarted(methodName: String) {
    Timber.d("[%s][%s] Task started", methodName, Thread.currentThread().name)
}

fun CoroutineScope.logTaskCompleted(methodName: String) {
    Timber.d("[%s][%s] Task completed", methodName, Thread.currentThread().name)
}