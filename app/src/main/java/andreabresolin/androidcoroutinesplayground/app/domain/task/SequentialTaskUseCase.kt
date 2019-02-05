package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.coroutines.backgroundTask
import andreabresolin.androidcoroutinesplayground.app.coroutines.delayTask
import andreabresolin.androidcoroutinesplayground.app.coroutines.ioTask
import andreabresolin.androidcoroutinesplayground.app.domain.BaseUseCase
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.app.repository.RemoteRepository
import javax.inject.Inject
import kotlin.random.Random

class SequentialTaskUseCase
@Inject constructor(
    private val remoteRepository: RemoteRepository
) : BaseUseCase() {

    suspend fun execute(
        startDelay: Long,
        minDuration: Long,
        maxDuration: Long
    ): TaskExecutionResult = backgroundTask {
        delayTask(startDelay)

        val taskDuration = Random.nextLong(minDuration, maxDuration + 1)

        val fetchedData = ioTask { remoteRepository.fetchData(taskDuration) }

        delayTask(taskDuration)

        return@backgroundTask TaskExecutionSuccess(fetchedData)
    }
}