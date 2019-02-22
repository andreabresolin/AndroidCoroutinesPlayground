package andreabresolin.androidcoroutinesplayground.app.repository

import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepository
@Inject constructor() {

    fun fetchData(input: Long): Long {
        return input * 10
    }

    fun fetchDataWithException(): Long {
        throw IOException("Error while reading repository data")
    }
}