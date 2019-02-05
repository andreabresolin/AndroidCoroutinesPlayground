package andreabresolin.androidcoroutinesplayground.app.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AppCoroutinesConfiguration {

    companion object {
        var uiDispatcher: CoroutineDispatcher = Dispatchers.Main
        var backgroundDispatcher: CoroutineDispatcher = Dispatchers.Default
        var ioDispatcher: CoroutineDispatcher = Dispatchers.IO
        var isDelayEnabled: Boolean = true
    }
}