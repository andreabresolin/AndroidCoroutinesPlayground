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
            AppCoroutinesConfiguration.uiDispatcher = Dispatchers.Unconfined
            AppCoroutinesConfiguration.backgroundDispatcher = Dispatchers.Unconfined
            AppCoroutinesConfiguration.ioDispatcher = Dispatchers.Unconfined
            AppCoroutinesConfiguration.isDelayEnabled = false
        }
    }

    protected lateinit var testAppCoroutineScope: TestAppCoroutineScope

    @Before
    fun beforeBaseMockitoTest() {
        testAppCoroutineScope = TestAppCoroutineScope()
    }
}