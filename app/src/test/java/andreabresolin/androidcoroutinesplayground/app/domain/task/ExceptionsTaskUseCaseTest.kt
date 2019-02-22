package andreabresolin.androidcoroutinesplayground.app.domain.task

import andreabresolin.androidcoroutinesplayground.app.exception.CustomTaskException
import andreabresolin.androidcoroutinesplayground.app.repository.RemoteRepository
import andreabresolin.androidcoroutinesplayground.testing.BaseMockitoTest
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.BDDMockito.given
import org.mockito.Mock
import java.io.IOException

class ExceptionsTaskUseCaseTest : BaseMockitoTest() {

    @Mock
    private lateinit var mockRemoteRepository: RemoteRepository

    private lateinit var subject: ExceptionsTaskUseCase

    private lateinit var actualThrownException: Exception

    @Before
    fun before() {
        subject = ExceptionsTaskUseCase(mockRemoteRepository)
    }

    // region Test

    @Test
    fun execute_throwsCustomTaskException() {
        whenExecute()
        thenThrownExceptionIs(CustomTaskException::class.java)
    }

    @Test
    fun executeAsync_throwsCustomTaskException() {
        whenExecuteAsync()
        thenThrownExceptionIs(CustomTaskException::class.java)
    }

    @Test
    fun executeWithRepositoryAsync_throwsIOException() {
        givenDataWillBeFetchedFromRepository()
        whenExecuteWithRepositoryAsync()
        thenThrownExceptionIs(IOException::class.java)
    }

    // endregion Test

    // region Given

    private fun givenDataWillBeFetchedFromRepository() {
        given(mockRemoteRepository.fetchData(anyLong())).willReturn(0)
        given(mockRemoteRepository.fetchDataWithException()).willAnswer { throw IOException() }
    }

    // endregion Given

    // region When

    private fun whenExecute() = runBlocking {
        try {
            subject.execute(1L, 1L, 1L)
        } catch (e: Exception) {
            actualThrownException = e
        }
    }

    private fun whenExecuteAsync() = runBlocking {
        try {
            subject.executeAsync(testAppCoroutineScope, 1L, 1L, 1L).await()
        } catch (e: Exception) {
            actualThrownException = e
        }
    }

    private fun whenExecuteWithRepositoryAsync() = runBlocking {
        try {
            subject.executeWithRepositoryAsync(testAppCoroutineScope, 1L, 1L, 1L).await()
        } catch (e: Exception) {
            actualThrownException = e
        }
    }

    // endregion When

    // region Then

    private fun <T: Exception> thenThrownExceptionIs(exception: Class<T>) = runBlocking {
        assertThat(actualThrownException).isInstanceOf(exception)
    }

    // endregion Then
}