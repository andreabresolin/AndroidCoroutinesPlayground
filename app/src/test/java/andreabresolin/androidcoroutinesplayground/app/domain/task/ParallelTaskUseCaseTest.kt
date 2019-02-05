package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.app.repository.RemoteRepository
import andreabresolin.androidcoroutinesplayground.testing.BaseMockitoTest
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.BDDMockito.given
import org.mockito.Mock

class ParallelTaskUseCaseTest : BaseMockitoTest() {

    @Mock
    private lateinit var mockRemoteRepository: RemoteRepository

    private lateinit var subject: ParallelTaskUseCase

    private lateinit var actualExecuteResult: Deferred<TaskExecutionResult>

    @Before
    fun before() {
        subject = ParallelTaskUseCase(mockRemoteRepository)
    }

    // region Test

    @Test
    fun executeAsync_executesTask() {
        givenFetchedDataIs(100)
        whenExecuteAsyncWith(10, 20, 30)
        thenResultIs(TaskExecutionSuccess(100))
    }

    // endregion Test

    // region Given

    private fun givenFetchedDataIs(result: Long) {
        given(mockRemoteRepository.fetchData(anyLong())).willReturn(result)
    }

    // endregion Given

    // region When

    private fun whenExecuteAsyncWith(startDelay: Long, minDuration: Long, maxDuration: Long) = runBlocking {
        actualExecuteResult = subject.executeAsync(testAppCoroutineScope, startDelay, minDuration, maxDuration)
    }

    // endregion When

    // region Then

    private fun thenResultIs(result: TaskExecutionResult) = runBlocking {
        assertThat(actualExecuteResult.await()).isEqualTo(result)
    }

    // endregion Then
}