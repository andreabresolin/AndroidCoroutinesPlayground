package andreabresolin.androidcoroutinesplayground.app.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren

abstract class BaseViewModel
constructor(private val coroutineScope: CoroutineScope) : ViewModel(), CoroutineScope by coroutineScope {

    override fun onCleared() {
        coroutineScope.coroutineContext.cancelChildren()
        super.onCleared()
    }
}