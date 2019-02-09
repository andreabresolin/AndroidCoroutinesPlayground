package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutinesConfiguration.Companion.TEST_TIMEOUT
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.app.util.DateTimeProvider
import andreabresolin.androidcoroutinesplayground.testing.BaseMockitoTest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import org.mockito.Mock

class LongComputationTaskUseCaseTest : BaseMockitoTest() {

    @Mock
    private lateinit var mockDateTimeProvider: DateTimeProvider

    private lateinit var subject: LongComputationTaskUseCase

    private var actualExecuteResult: TaskExecutionResult? = null
    private var actualExecuteException: Exception? = null

    @Before
    fun before() {
        actualExecuteResult = null
        actualExecuteException = null

        subject = LongComputationTaskUseCase(mockDateTimeProvider)
    }

    // region Test

    @Test
    fun executeAsync_executesTask() {
        givenExecuteAsyncWillHandleIterations(300, 10)
        whenExecuteAsyncWith(300, 10, 0)
        thenResultIs(TaskExecutionSuccess(10))
        thenIterationsCountIs(10)
    }

    @Test
    fun executeAsync_executesTaskUntilCancelled() {
        givenExecuteAsyncWillHandleIterationsAndThenBeCancelled(300, 5)
        whenExecuteAsyncWith(300, 10, 0)
        thenTaskCancelled()
        thenIterationsCountIs(5)
    }

    @Test
    fun executeAsync_executesTaskUntilTimeout() {
        givenExecuteAsyncWillHandleIterationsAndLastMoreThan(300, 5, TEST_TIMEOUT + 200L)
        whenExecuteAsyncWith(300, 10, TEST_TIMEOUT)
        thenTaskCancelled()
        thenIterationsCountIs(5)
    }

    // endregion Test

    // region Given

    private fun givenExecuteAsyncWillHandleIterations(iterationDuration: Int,
                                                      iterationsCount: Int): BDDMyOngoingStubbing<Long> {
        var currentTime = 0L
        var ongoingStubbing = given(mockDateTimeProvider.currentTimeMillis()).willReturn(currentTime)

        repeat(iterationsCount) {
            ongoingStubbing = ongoingStubbing.willReturn(currentTime)
            currentTime += iterationDuration
        }

        return ongoingStubbing
    }

    private fun givenExecuteAsyncWillHandleIterationsAndThenBeCancelled(iterationDuration: Int,
                                                                        cancellationIterationNumber: Int) {
        givenExecuteAsyncWillHandleIterations(iterationDuration, cancellationIterationNumber - 1).willAnswer {
            testAppCoroutineScope.cancelJobs()
            return@willAnswer 0L
        }
    }

    private fun givenExecuteAsyncWillHandleIterationsAndLastMoreThan(iterationDuration: Int,
                                                                     iterationsCount: Int,
                                                                     minimumExecutionDuration: Long) {
        givenExecuteAsyncWillHandleIterations(iterationDuration, iterationsCount - 1).willAnswer {
            return@willAnswer runBlocking {
                delay(minimumExecutionDuration)
                return@runBlocking 0L
            }
        }
    }

    // endregion Given

    // region When

    private fun whenExecuteAsyncWith(iterationDuration: Long,
                                     iterationsCount: Long,
                                     timeout: Long) = runBlocking {
        try {
            actualExecuteResult = subject.executeAsync(testAppCoroutineScope, iterationDuration, iterationsCount, timeout).await()
        } catch (e: Exception) {
            actualExecuteException = e
        }
    }

    // endregion When

    // region Then

    private fun thenResultIs(result: TaskExecutionResult) = runBlocking {
        assertThat(actualExecuteResult).isEqualTo(result)
    }

    private fun thenTaskCancelled() {
        assertThat(actualExecuteException).isInstanceOf(CancellationException::class.java)
    }

    private fun thenIterationsCountIs(iterationsCount: Int) {
        then(mockDateTimeProvider).should(times(iterationsCount + 1)).currentTimeMillis()
        then(mockDateTimeProvider).shouldHaveNoMoreInteractions()
    }

    // endregion Then
}
