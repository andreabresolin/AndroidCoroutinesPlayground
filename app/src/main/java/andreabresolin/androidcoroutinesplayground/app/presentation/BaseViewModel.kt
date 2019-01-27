package andreabresolin.androidcoroutinesplayground.app.presentation

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutineScope
import androidx.lifecycle.ViewModel

abstract class BaseViewModel
constructor(appCoroutineScope: AppCoroutineScope) : ViewModel(), AppCoroutineScope by appCoroutineScope {

    override fun onCleared() {
        cancelAllTasks()
        super.onCleared()
    }
}