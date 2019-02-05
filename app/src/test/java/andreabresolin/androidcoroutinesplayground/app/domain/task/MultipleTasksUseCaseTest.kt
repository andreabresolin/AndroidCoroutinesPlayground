package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.app.repository.RemoteRepository
import andreabresolin.androidcoroutinesplayground.testing.BaseMockitoTest
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock

class MultipleTasksUseCaseTest : BaseMockitoTest() {

    @Mock
    private lateinit var mockRemoteRepository: RemoteRepository

    private lateinit var subject: MultipleTasksUseCase

    private lateinit var actualExecuteResult: TaskExecutionResult

    @Before
    fun before() {
        subject = MultipleTasksUseCase(mockRemoteRepository)
    }

    // region Test

    @Test
    fun execute_executesTasks() {
        givenRemoteRepositoryWithInputWillReturn(1, 10)
        givenRemoteRepositoryWithInputWillReturn(2, 20)
        givenRemoteRepositoryWithInputWillReturn(3, 30)
        givenRemoteRepositoryWithInputWillReturn(10, 100)
        givenRemoteRepositoryWithInputWillReturn(20, 200)
        givenRemoteRepositoryWithInputWillReturn(30, 300)
        whenExecuteWith(1, 2, 3)
        thenResultIs(TaskExecutionSuccess(600))
    }

    // endregion Test

    // region Given

    private fun givenRemoteRepositoryWithInputWillReturn(input: Long, result: Long) {
        given(mockRemoteRepository.fetchData(input)).willReturn(result)
    }

    // endregion Given

    // region When

    private fun whenExecuteWith(param1: Long, param2: Long, param3: Long) = runBlocking {
        actualExecuteResult = subject.execute(param1, param2, param3)
    }

    // endregion When

    // region Then

    private fun thenResultIs(result: TaskExecutionResult) {
        assertThat(actualExecuteResult).isEqualTo(result)
    }

    // endregion Then
}