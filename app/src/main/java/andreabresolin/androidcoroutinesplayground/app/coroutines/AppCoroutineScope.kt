package andreabresolin.androidcoroutinesplayground.app.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class AppCoroutineScope
@Inject constructor() : CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = AppCoroutinesConfiguration.uiDispatcher + job
}