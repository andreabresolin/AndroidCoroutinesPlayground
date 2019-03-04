package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.exception.CustomTaskException
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.testing.BaseMockitoTest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class CallbackTaskUseCaseTest : BaseMockitoTest() {

    private lateinit var subject: CallbackTaskUseCase

    private var actualExecuteResult: TaskExecutionResult? = null
    private var actualExecuteException: Exception? = null

    @Before
    fun before() {
        subject = CallbackTaskUseCase()
        actualExecuteResult = null
        actualExecuteException = null
    }

    // region Test

    @Test
    fun execute_executesTaskWithSuccess() {
        whenExecuteWith("SUCCESS")
        thenResultIs(TaskExecutionSuccess(10L))
        thenNoException()
    }

    @Test
    fun execute_executesTaskWithCancellation() {
        whenExecuteWith("CANCEL")
        thenResultIsNull()
        thenExceptionIsInstanceOf(CancellationException::class.java)
    }

    @Test
    fun execute_executesTaskWithError() {
        whenExecuteWith("ANOTHER INPUT")
        thenResultIsNull()
        thenExceptionIsInstanceOf(CustomTaskException::class.java)
    }

    // endregion Test

    // region When

    private fun whenExecuteWith(param: String) = runBlocking {
        try {
            actualExecuteResult = subject.execute(param)
        } catch (e: Exception) {
            actualExecuteException = e
        }
    }

    // endregion When

    // region Then

    private fun thenResultIs(result: TaskExecutionResult) {
        assertThat(actualExecuteResult).isEqualTo(result)
    }

    private fun thenResultIsNull() {
        assertThat(actualExecuteResult).isNull()
    }

    private fun <T> thenExceptionIsInstanceOf(type: Class<T>) {
        assertThat(actualExecuteException).isInstanceOf(type)
    }

    private fun thenNoException() {
        assertThat(actualExecuteException).isNull()
    }

    // endregion Then
}