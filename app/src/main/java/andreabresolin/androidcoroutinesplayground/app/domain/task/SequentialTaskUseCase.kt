package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutineScope
import andreabresolin.androidcoroutinesplayground.app.domain.BaseCoroutineUseCase
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.app.repository.RemoteRepository
import javax.inject.Inject
import kotlin.random.Random

class SequentialTaskUseCase
@Inject constructor(
    appCoroutineScope: AppCoroutineScope,
    private val remoteRepository: RemoteRepository
) : BaseCoroutineUseCase(appCoroutineScope) {

    suspend fun execute(startDelay: Long, minDuration: Long, maxDuration: Long): TaskExecutionResult = backgroundTask {
        delayTask(startDelay)

        val taskDuration = Random.nextLong(minDuration, maxDuration + 1)

        val fetchedData = ioTask { remoteRepository.fetchData(taskDuration) }

        delayTask(taskDuration)

        return@backgroundTask TaskExecutionSuccess(fetchedData)
    }
}