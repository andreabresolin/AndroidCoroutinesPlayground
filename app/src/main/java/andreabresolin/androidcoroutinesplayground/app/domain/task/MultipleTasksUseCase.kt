package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.coroutines.backgroundTask
import andreabresolin.androidcoroutinesplayground.app.coroutines.delayTask
import andreabresolin.androidcoroutinesplayground.app.coroutines.ioTask
import andreabresolin.androidcoroutinesplayground.app.coroutines.ioTaskAsync
import andreabresolin.androidcoroutinesplayground.app.domain.BaseUseCase
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.app.repository.RemoteRepository
import javax.inject.Inject
import kotlin.random.Random

class MultipleTasksUseCase
@Inject constructor(
    private val remoteRepository: RemoteRepository
) : BaseUseCase() {

    suspend fun execute(param1: Long, param2: Long, param3: Long): TaskExecutionResult = backgroundTask {
        val taskDuration = Random.nextLong(1000, 2000)
        delayTask(taskDuration)

        val fetchedData: Long = arrayOf(param1, param2, param3)
            .map { ioTaskAsync { remoteRepository.fetchData(it) } }
            .map { it.await() }
            .map { ioTask { remoteRepository.fetchData(it) } }
            .sum()

        return@backgroundTask TaskExecutionSuccess(fetchedData)
    }
}