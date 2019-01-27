package andreabresolin.androidcoroutinesplayground.app.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatcherProvider {

    val main: CoroutineDispatcher

    val background: CoroutineDispatcher

    val io: CoroutineDispatcher
}