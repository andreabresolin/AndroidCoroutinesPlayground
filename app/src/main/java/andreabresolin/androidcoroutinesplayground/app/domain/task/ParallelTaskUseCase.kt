package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.coroutines.backgroundTaskAsync
import andreabresolin.androidcoroutinesplayground.app.coroutines.delayTask
import andreabresolin.androidcoroutinesplayground.app.coroutines.ioTask
import andreabresolin.androidcoroutinesplayground.app.domain.BaseUseCase
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.app.repository.RemoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import javax.inject.Inject
import kotlin.random.Random

class ParallelTaskUseCase
@Inject constructor(
    private val remoteRepository: RemoteRepository
) : BaseUseCase() {

    fun executeAsync(
        coroutineScope: CoroutineScope,
        startDelay: Long,
        minDuration: Long,
        maxDuration: Long
    ): Deferred<TaskExecutionResult> = coroutineScope.backgroundTaskAsync {
        delayTask(startDelay)

        val taskDuration = Random.nextLong(minDuration, maxDuration + 1)

        val fetchedData = ioTask { remoteRepository.fetchData(taskDuration) }

        delayTask(taskDuration)

        return@backgroundTaskAsync TaskExecutionSuccess(fetchedData)
    }
}