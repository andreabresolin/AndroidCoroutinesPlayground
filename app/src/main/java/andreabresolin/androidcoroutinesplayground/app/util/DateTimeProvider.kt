package andreabresolin.androidcoroutinesplayground.app.util

import javax.inject.Inject

class DateTimeProvider
@Inject constructor() {

    fun currentTimeMillis() = System.currentTimeMillis()
}