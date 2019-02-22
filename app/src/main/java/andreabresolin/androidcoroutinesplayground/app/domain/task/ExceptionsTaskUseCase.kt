package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.coroutines.*
import andreabresolin.androidcoroutinesplayground.app.domain.BaseUseCase
import andreabresolin.androidcoroutinesplayground.app.exception.CustomTaskException
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.app.repository.RemoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import javax.inject.Inject
import kotlin.random.Random

class ExceptionsTaskUseCase
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

        delayTask(taskDuration)

        throw CustomTaskException("Error in ExceptionsTaskUseCase.execute()")
    }

    fun executeAsync(
        parentScope: CoroutineScope,
        startDelay: Long,
        minDuration: Long,
        maxDuration: Long
    ): Deferred<TaskExecutionResult> = parentScope.backgroundTaskAsync {
        delayTask(startDelay)

        val taskDuration = Random.nextLong(minDuration, maxDuration + 1)

        delayTask(taskDuration)

        throw CustomTaskException("Error in ExceptionsTaskUseCase.executeAsync()")
    }

    fun executeWithRepositoryAsync(
        parentScope: CoroutineScope,
        startDelay: Long,
        minDuration: Long,
        maxDuration: Long
    ): Deferred<TaskExecutionResult> = parentScope.backgroundTaskAsync {
        delayTask(startDelay)

        val taskDuration = Random.nextLong(minDuration, maxDuration + 1)

        val fetchedData1 = ioTaskAsync {
            delayTask(2000)
            remoteRepository.fetchDataWithException()
        }

        val fetchedData2 = ioTaskAsync {
            delayTask(5000)
            remoteRepository.fetchData(taskDuration)
        }

        delayTask(taskDuration)

        return@backgroundTaskAsync TaskExecutionSuccess(awaitAllOrCancel(fetchedData1, fetchedData2).sum())
    }
}