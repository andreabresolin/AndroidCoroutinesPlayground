package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutineScope
import andreabresolin.androidcoroutinesplayground.app.domain.BaseUseCase
import andreabresolin.androidcoroutinesplayground.app.exception.CustomTaskException
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionError
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import kotlinx.coroutines.Deferred
import javax.inject.Inject
import kotlin.random.Random

class ParallelErrorTaskUseCase
@Inject constructor(
    appCoroutineScope: AppCoroutineScope
) : BaseUseCase(appCoroutineScope) {

    fun execute(startDelay: Long, minDuration: Long, maxDuration: Long): Deferred<TaskExecutionResult> = parallelTask {
        delayTask(startDelay)

        val taskDuration = Random.nextLong(minDuration, maxDuration + 1)
        delayTask(taskDuration)

        return@parallelTask TaskExecutionError(CustomTaskException())
    }
}