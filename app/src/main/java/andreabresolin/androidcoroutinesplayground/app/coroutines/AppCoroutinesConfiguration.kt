package andreabresolin.androidcoroutinesplayground.app.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AppCoroutinesConfiguration {

    companion object {
        const val TEST_TIMEOUT: Long = 500L

        var uiDispatcher: CoroutineDispatcher = Dispatchers.Main
        var backgroundDispatcher: CoroutineDispatcher = Dispatchers.Default
        var ioDispatcher: CoroutineDispatcher = Dispatchers.IO
        var isDelayEnabled: Boolean = true
        var useTestTimeout: Boolean = false
        var isLoggingEnabled: Boolean = true
    }
}