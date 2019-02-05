package andreabresolin.androidcoroutinesplayground.app.util

import timber.log.Timber

fun logStarted(methodName: String) {
    Timber.d("[%s][%s] Started", methodName, Thread.currentThread().name)
}

fun logCompleted(methodName: String) {
    Timber.d("[%s][%s] Completed", methodName, Thread.currentThread().name)
}

fun logCancelled(methodName: String) {
    Timber.d("[%s][%s] Cancelled", methodName, Thread.currentThread().name)
}

fun logIteration(methodName: String, iterationNumber: Long) {
    Timber.d("[%s][%s] Iteration %s", methodName, Thread.currentThread().name, iterationNumber.toString())
}