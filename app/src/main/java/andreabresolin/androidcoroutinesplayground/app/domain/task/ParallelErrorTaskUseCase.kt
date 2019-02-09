package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.coroutines.backgroundTaskAsync
import andreabresolin.androidcoroutinesplayground.app.coroutines.delayTask
import andreabresolin.androidcoroutinesplayground.app.domain.BaseUseCase
import andreabresolin.androidcoroutinesplayground.app.exception.CustomTaskException
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionError
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import javax.inject.Inject
import kotlin.random.Random

class ParallelErrorTaskUseCase
@Inject constructor() : BaseUseCase() {

    fun executeAsync(
        parentScope: CoroutineScope,
        startDelay: Long,
        minDuration: Long,
        maxDuration: Long
    ): Deferred<TaskExecutionResult> = parentScope.backgroundTaskAsync {
        delayTask(startDelay)

        val taskDuration = Random.nextLong(minDuration, maxDuration + 1)
        delayTask(taskDuration)

        return@backgroundTaskAsync TaskExecutionError(CustomTaskException())
    }
}