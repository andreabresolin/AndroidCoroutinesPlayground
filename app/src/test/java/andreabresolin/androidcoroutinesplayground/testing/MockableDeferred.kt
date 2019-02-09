package andreabresolin.androidcoroutinesplayground.testing

import kotlinx.coroutines.Deferred

abstract class MockableDeferred<T> : Deferred<T> {

    override fun cancel() {
        // Do nothing
    }
}