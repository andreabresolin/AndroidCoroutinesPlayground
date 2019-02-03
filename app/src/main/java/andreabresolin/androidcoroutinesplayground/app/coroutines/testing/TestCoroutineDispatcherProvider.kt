package andreabresolin.androidcoroutinesplayground.app.coroutines.testing

import andreabresolin.androidcoroutinesplayground.app.coroutines.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class TestCoroutineDispatcherProvider
@Inject constructor() : CoroutineDispatcherProvider {

    override val main: CoroutineDispatcher
        get() = Dispatchers.Unconfined

    override val background: CoroutineDispatcher
        get() = Dispatchers.Unconfined

    override val io: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}