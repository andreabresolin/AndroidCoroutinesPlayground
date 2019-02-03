package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionError
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.base.BaseMockitoTest
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class CallbackTaskUseCaseTest : BaseMockitoTest() {

    private lateinit var subject: CallbackTaskUseCase

    private lateinit var actualExecuteResult: TaskExecutionResult

    @Before
    fun before() {
        subject = CallbackTaskUseCase(testAppCoroutineScope)
    }

    // region Test

    @Test
    fun execute_executesTaskWithSuccess() {
        whenExecuteWith("SUCCESS")
        thenResultIs(TaskExecutionSuccess(10))
    }

    @Test
    fun execute_executesTaskWithError() {
        whenExecuteWith("ANOTHER INPUT")
        thenResultIsInstanceOf(TaskExecutionError::class.java)
    }

    // endregion Test

    // region When

    private fun whenExecuteWith(param: String) = runBlocking {
        actualExecuteResult = subject.execute(param)
    }

    // endregion When

    // region Then

    private fun thenResultIs(result: TaskExecutionResult) {
        assertThat(actualExecuteResult).isEqualTo(result)
    }

    private fun <T> thenResultIsInstanceOf(type: Class<T>) {
        assertThat(actualExecuteResult).isInstanceOf(type)
    }

    // endregion Then
}