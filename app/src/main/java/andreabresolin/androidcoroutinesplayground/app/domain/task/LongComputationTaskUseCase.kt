package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.coroutines.backgroundTaskAsync
import andreabresolin.androidcoroutinesplayground.app.domain.BaseUseCase
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.app.util.DateTimeProvider
import andreabresolin.androidcoroutinesplayground.app.util.logCancelled
import andreabresolin.androidcoroutinesplayground.app.util.logIteration
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.isActive
import javax.inject.Inject

class LongComputationTaskUseCase
@Inject constructor(
    private val dateTimeProvider: DateTimeProvider
) : BaseUseCase() {

    fun executeAsync(
        parentScope: CoroutineScope,
        iterationDuration: Long,
        iterationsCount: Long,
        timeout: Long = 0L
    ): Deferred<TaskExecutionResult> = parentScope.backgroundTaskAsync(timeout = timeout) {
        var iterationNumber = 1L
        var nextIterationTime = dateTimeProvider.currentTimeMillis()

        while (isActive && iterationNumber <= iterationsCount) {
            if (dateTimeProvider.currentTimeMillis() >= nextIterationTime) {
                logIteration("LongComputationTaskUseCase.executeAsync@$parentScope", iterationNumber)
                nextIterationTime += iterationDuration
                iterationNumber++
            }
        }

        if (!isActive) {
            logCancelled("LongComputationTaskUseCase.executeAsync@$parentScope")
            throw CancellationException()
        }

        return@backgroundTaskAsync TaskExecutionSuccess(iterationNumber - 1)
    }
}