package andreabresolin.androidcoroutinesplayground.app.coroutines.test

import andreabresolin.androidcoroutinesplayground.app.coroutines.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class TestCoroutineDispatcherProvider
@Inject constructor(): CoroutineDispatcherProvider {

    override val main: CoroutineDispatcher
        get() = Dispatchers.Main

    override val background: CoroutineDispatcher
        get() = Dispatchers.Main

    override val io: CoroutineDispatcher
        get() = Dispatchers.Main
}