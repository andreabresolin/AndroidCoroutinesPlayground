package andreabresolin.androidcoroutinesplayground.app.repository

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepository
@Inject constructor() {

    fun fetchData(input: Long): Long {
        return input * 10
    }
}