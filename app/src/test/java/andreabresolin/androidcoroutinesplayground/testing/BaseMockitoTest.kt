package andreabresolin.androidcoroutinesplayground.testing

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutinesConfiguration
import andreabresolin.androidcoroutinesplayground.app.coroutines.testing.TestAppCoroutineScope
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
abstract class BaseMockitoTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun beforeClassBaseMockitoTest() {
            with (AppCoroutinesConfiguration) {
                uiDispatcher = Dispatchers.Unconfined
                backgroundDispatcher = Dispatchers.Unconfined
                ioDispatcher = Dispatchers.Unconfined
                isDelayEnabled = false
                useTestTimeout = true
            }
        }
    }

    protected lateinit var testAppCoroutineScope: TestAppCoroutineScope

    @Before
    fun beforeBaseMockitoTest() {
        testAppCoroutineScope = TestAppCoroutineScope()
    }
}