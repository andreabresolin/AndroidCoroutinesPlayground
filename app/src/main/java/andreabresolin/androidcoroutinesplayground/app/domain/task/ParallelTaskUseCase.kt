package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutineScope
import andreabresolin.androidcoroutinesplayground.app.domain.BaseCoroutineUseCase
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.app.repository.RemoteRepository
import kotlinx.coroutines.Deferred
import javax.inject.Inject
import kotlin.random.Random

class ParallelTaskUseCase
@Inject constructor(
    appCoroutineScope: AppCoroutineScope,
    private val remoteRepository: RemoteRepository
) : BaseCoroutineUseCase(appCoroutineScope) {

    fun executeAsync(startDelay: Long, minDuration: Long, maxDuration: Long): Deferred<TaskExecutionResult> = backgroundTaskAsync {
        delayTask(startDelay)

        val taskDuration = Random.nextLong(minDuration, maxDuration + 1)

        val fetchedData = ioTask { remoteRepository.fetchData(taskDuration) }

        delayTask(taskDuration)

        return@backgroundTaskAsync TaskExecutionSuccess(fetchedData)
    }
}