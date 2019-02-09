package andreabresolin.androidcoroutinesplayground.app.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren

abstract class BasePresenter
constructor(private val coroutineScope: CoroutineScope) : CoroutineScope by coroutineScope {

    fun cancelJobs() {
        coroutineScope.coroutineContext.cancelChildren()
    }
}