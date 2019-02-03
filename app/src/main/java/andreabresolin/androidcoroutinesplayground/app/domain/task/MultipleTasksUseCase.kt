package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutineScope
import andreabresolin.androidcoroutinesplayground.app.domain.BaseCoroutineUseCase
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.app.repository.RemoteRepository
import javax.inject.Inject
import kotlin.random.Random

class MultipleTasksUseCase
@Inject constructor(
    appCoroutineScope: AppCoroutineScope,
    private val remoteRepository: RemoteRepository
) : BaseCoroutineUseCase(appCoroutineScope) {

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