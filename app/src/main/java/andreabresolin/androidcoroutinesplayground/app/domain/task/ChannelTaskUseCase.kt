package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.coroutines.backgroundTaskAsync
import andreabresolin.androidcoroutinesplayground.app.coroutines.delayTask
import andreabresolin.androidcoroutinesplayground.app.domain.BaseUseCase
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.app.util.logIteration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.selects.select
import javax.inject.Inject

class ChannelTaskUseCase
@Inject constructor() : BaseUseCase() {

    fun executeAsync(
        parentScope: CoroutineScope,
        sendInterval: Long,
        sentItemsCount: Long,
        primaryChannel: SendChannel<Long>,
        backupChannel: SendChannel<Long>? = null
    ): Deferred<TaskExecutionResult> = parentScope.backgroundTaskAsync {
        var iterationNumber = 1L

        while (iterationNumber <= sentItemsCount) {
            logIteration("ChannelTaskUseCase.executeAsync@$parentScope", iterationNumber)
            delayTask(sendInterval)

            if (backupChannel != null) {
                select<Unit> {
                    primaryChannel.onSend(iterationNumber) { }
                    backupChannel.onSend(iterationNumber) { }
                }
            } else {
                primaryChannel.send(iterationNumber)
            }

            iterationNumber++
        }

        primaryChannel.close()
        backupChannel?.close()

        return@backgroundTaskAsync TaskExecutionSuccess(sentItemsCount)
    }
}