package andreabresolin.androidcoroutinesplayground.app.coroutines.testing

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TestAppCoroutineScope
@Inject constructor() : AppCoroutineScope() {

    private val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Unconfined + job

    fun cancelTasks() {
        coroutineContext.cancelChildren()
    }
}