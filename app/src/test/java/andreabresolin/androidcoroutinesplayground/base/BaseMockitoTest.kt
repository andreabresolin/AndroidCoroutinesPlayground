package andreabresolin.androidcoroutinesplayground.base

import andreabresolin.androidcoroutinesplayground.app.coroutines.test.TestAppCoroutineScope
import andreabresolin.androidcoroutinesplayground.app.coroutines.test.TestCoroutineDispatcherProvider
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
abstract class BaseMockitoTest {

    protected val appCoroutineScope = TestAppCoroutineScope(TestCoroutineDispatcherProvider())
}