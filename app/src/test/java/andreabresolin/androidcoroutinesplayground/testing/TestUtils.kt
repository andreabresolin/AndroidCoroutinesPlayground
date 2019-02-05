package andreabresolin.androidcoroutinesplayground.testing

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
    }
}