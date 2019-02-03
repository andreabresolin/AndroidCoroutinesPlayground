package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.app.repository.RemoteRepository
import andreabresolin.androidcoroutinesplayground.base.BaseMockitoTest
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.anyLong
import org.mockito.BDDMockito.given
import org.mockito.Mock

class SequentialTaskUseCaseTest : BaseMockitoTest() {

    @Mock
    private lateinit var mockRemoteRepository: RemoteRepository

    private lateinit var subject: SequentialTaskUseCase

    private lateinit var actualExecuteResult: TaskExecutionResult

    @Before
    fun before() {
        subject = SequentialTaskUseCase(
            testAppCoroutineScope,
            mockRemoteRepository)
    }

    // region Test

    @Test
    fun execute_executesTask() {
        givenRemoteRepositoryWillReturn(100)
        whenExecuteWith(10, 20, 30)
        thenResultIs(TaskExecutionSuccess(100))
    }

    // endregion Test

    // region Given

    private fun givenRemoteRepositoryWillReturn(result: Long) {
        given(mockRemoteRepository.fetchData(anyLong())).willReturn(result)
    }

    // endregion Given

    // region When

    private fun whenExecuteWith(startDelay: Long, minDuration: Long, maxDuration: Long) = runBlocking {
        actualExecuteResult = subject.execute(startDelay, minDuration, maxDuration)
    }

    // endregion When

    // region Then

    private fun thenResultIs(result: TaskExecutionResult) {
        assertThat(actualExecuteResult).isEqualTo(result)
    }

    // endregion Then
}