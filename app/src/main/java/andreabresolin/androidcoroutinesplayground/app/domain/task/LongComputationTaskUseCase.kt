package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutineScope
import andreabresolin.androidcoroutinesplayground.app.domain.BaseCoroutineUseCase
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.app.util.DateTimeProvider
import andreabresolin.androidcoroutinesplayground.app.util.logCancelled
import andreabresolin.androidcoroutinesplayground.app.util.logIteration
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.isActive
import javax.inject.Inject

class LongComputationTaskUseCase
@Inject constructor(
    appCoroutineScope: AppCoroutineScope,
    private val dateTimeProvider: DateTimeProvider
) : BaseCoroutineUseCase(appCoroutineScope) {

    fun executeAsync(iterationDuration: Long, iterationsCount: Long): Deferred<TaskExecutionResult> = backgroundTaskAsync {
        var iterationNumber = 1L
        var nextIterationTime = dateTimeProvider.currentTimeMillis()

        while (isActive && iterationNumber <= iterationsCount) {
            if (dateTimeProvider.currentTimeMillis() >= nextIterationTime) {
                logIteration("LongComputationTaskUseCase.execute", iterationNumber)
                nextIterationTime += iterationDuration
                iterationNumber++
            }
        }

        if (!isActive) {
            logCancelled("LongComputationTaskUseCase.execute")
            throw CancellationException()
        }

        return@backgroundTaskAsync TaskExecutionSuccess(iterationNumber - 1)
    }
}