package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.testing.BaseMockitoTest
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class ChannelTaskUseCaseTest : BaseMockitoTest() {

    private lateinit var subject: ChannelTaskUseCase

    private lateinit var givenPrimaryChannel: Channel<Long>
    private var givenBackupChannel: Channel<Long>? = null

    private val actualPrimaryChannelItems = mutableListOf<Long>()
    private val actualBackupChannelItems = mutableListOf<Long>()
    private var actualExecuteAsyncResult: TaskExecutionResult? = null

    @Before
    fun before() {
        subject = ChannelTaskUseCase()
    }

    // region Test

    @Test
    fun executeAsync_sendsItemsOnPrimaryChannel() {
        givenPrimaryChannelIsAvailable()
        givenBackupChannelIsNotAvailable()
        whenExecuteAsyncWith(10)
        thenItemsSentOnPrimaryChannelAre(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        thenResultIs(TaskExecutionSuccess(10))
        thenPrimaryChannelIsClosed()
    }

    @Test
    fun executeAsync_sendsItemsOnPrimaryAndBackupChannels() {
        givenPrimaryChannelIsAvailable()
        givenBackupChannelIsAvailable()
        whenExecuteAsyncWith(6, 4)
        thenItemsSentOnPrimaryChannelAre(1, 2, 3, 4, 5, 6)
        thenItemsSentOnBackupChannelAre(7, 8, 9, 10)
        thenResultIs(TaskExecutionSuccess(10))
        thenPrimaryChannelIsClosed()
        thenBackupChannelIsClosed()
    }

    // endregion Test

    // region Given

    private fun givenPrimaryChannelIsAvailable() {
        givenPrimaryChannel = Channel()
    }

    private fun givenBackupChannelIsAvailable() {
        givenBackupChannel = Channel()
    }

    private fun givenBackupChannelIsNotAvailable() {
        givenBackupChannel = null
    }

    // endregion Given

    // region When

    private fun whenExecuteAsyncWith(primaryChannelSentItemsCount: Long, backupChannelSentItemsCount: Long = 0L) = runBlocking {
        val primaryChannelConsumer = testAppCoroutineScope.async(Dispatchers.Default) {
            actualPrimaryChannelItems.clear()

            for (receivedItem in givenPrimaryChannel) {
                actualPrimaryChannelItems.add(receivedItem)

                if (backupChannelSentItemsCount > 0 && receivedItem == primaryChannelSentItemsCount) {
                    break
                }
            }
        }

        var backupChannelConsumer: Deferred<Unit>? = null
        givenBackupChannel?.let { backupChannel ->
            backupChannelConsumer = testAppCoroutineScope.async(Dispatchers.Default) {
                primaryChannelConsumer.await()

                actualBackupChannelItems.clear()

                for (receivedItem in backupChannel) {
                    actualBackupChannelItems.add(receivedItem)
                }
            }
        }

        actualExecuteAsyncResult = subject.executeAsync(
            testAppCoroutineScope,
            0L,
            primaryChannelSentItemsCount + backupChannelSentItemsCount,
            givenPrimaryChannel,
            givenBackupChannel).await()

        primaryChannelConsumer.await()
        backupChannelConsumer?.await()
    }

    // endregion When

    // region Then

    private fun thenItemsSentOnPrimaryChannelAre(vararg items: Long) {
        assertThat(actualPrimaryChannelItems).isEqualTo(items.toList())
    }

    private fun thenItemsSentOnBackupChannelAre(vararg items: Long) {
        assertThat(actualBackupChannelItems).isEqualTo(items.toList())
    }

    private fun thenResultIs(result: TaskExecutionResult) {
        assertThat(actualExecuteAsyncResult).isEqualTo(result)
    }

    private fun thenPrimaryChannelIsClosed() {
        assertThat(givenPrimaryChannel.isClosedForSend).isTrue()
    }

    private fun thenBackupChannelIsClosed() {
        assertThat(givenBackupChannel).isNotNull
        assertThat(givenBackupChannel?.isClosedForSend).isTrue()
    }

    // endregion Then
}