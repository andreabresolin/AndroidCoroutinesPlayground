package andreabresolin.androidcoroutinesplayground.testing

import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq

interface KotlinTestUtils {

    companion object {
        fun <T> eqObj(obj: T): T {
            eq(obj)
            return obj
        }

        inline fun <reified T> anyObj(obj: T): T {
            any(T::class.java)
            return obj
        }

        fun <T> captureObj(captor: ArgumentCaptor<T>, obj: T): T {
            captor.capture()
            return obj
        }
    }
}