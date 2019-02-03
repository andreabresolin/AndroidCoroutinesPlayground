package andreabresolin.androidcoroutinesplayground.base

import andreabresolin.androidcoroutinesplayground.app.coroutines.testing.TestAppCoroutineScope
import andreabresolin.androidcoroutinesplayground.app.coroutines.testing.TestCoroutineDispatcherProvider
import org.junit.runner.RunWith
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
abstract class BaseMockitoTest {

    @Spy
    protected val testAppCoroutineScope = TestAppCoroutineScope(TestCoroutineDispatcherProvider())
}