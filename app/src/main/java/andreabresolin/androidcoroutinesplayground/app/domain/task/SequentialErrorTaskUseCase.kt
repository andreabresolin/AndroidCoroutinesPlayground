package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.coroutines.backgroundTask
import andreabresolin.androidcoroutinesplayground.app.coroutines.delayTask
import andreabresolin.androidcoroutinesplayground.app.domain.BaseUseCase
import andreabresolin.androidcoroutinesplayground.app.exception.CustomTaskException
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionError
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import javax.inject.Inject
import kotlin.random.Random

class SequentialErrorTaskUseCase
@Inject constructor() : BaseUseCase() {

    suspend fun execute(
        startDelay: Long,
        minDuration: Long,
        maxDuration: Long
    ): TaskExecutionResult = backgroundTask {
        delayTask(startDelay)

        val taskDuration = Random.nextLong(minDuration, maxDuration + 1)
        delayTask(taskDuration)

        return@backgroundTask TaskExecutionError(CustomTaskException())
    }
}