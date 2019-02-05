package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.app.util.DateTimeProvider
import andreabresolin.androidcoroutinesplayground.testing.BaseMockitoTest
import kotlinx.coroutines.CancellationException
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
        givenExecutionWillHandleIterations(300, 10)
        whenExecuteAsyncWith(300, 10)
        thenResultIs(TaskExecutionSuccess(10))
        thenIterationsCountIs(10)
    }

    @Test
    fun executeAsync_executesTaskUntilCancelled() {
        givenExecutionWillHandleIterationsAndThenBeCancelled(300, 5)
        whenExecuteAsyncWith(300, 10)
        thenTaskCancelled()
        thenIterationsCountIs(5)
    }

    // endregion Test

    // region Given

    private fun givenExecutionWillHandleIterations(iterationDuration: Int,
                                                   iterationsCount: Int): BDDMyOngoingStubbing<Long> {
        var currentTime = 0L
        var ongoingStubbing = given(mockDateTimeProvider.currentTimeMillis()).willReturn(currentTime)

        repeat(iterationsCount) {
            ongoingStubbing = ongoingStubbing.willReturn(currentTime)
            currentTime += iterationDuration
        }

        return ongoingStubbing
    }

    private fun givenExecutionWillHandleIterationsAndThenBeCancelled(iterationDuration: Int,
                                                                     cancellationIterationNumber: Int) {
        givenExecutionWillHandleIterations(iterationDuration, cancellationIterationNumber - 1).willAnswer {
            testAppCoroutineScope.cancelTasks()
            return@willAnswer 0L
        }
    }

    // endregion Given

    // region When

    private fun whenExecuteAsyncWith(iterationDuration: Long, iterationsCount: Long) = runBlocking {
        try {
            actualExecuteResult = subject.executeAsync(testAppCoroutineScope, iterationDuration, iterationsCount).await()
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
