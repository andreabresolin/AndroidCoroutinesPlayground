package andreabresolin.androidcoroutinesplayground.app.coroutines

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutinesHelpers.Companion.startJob
import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutinesHelpers.Companion.startTask
import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutinesHelpers.Companion.startTaskAsync
import andreabresolin.androidcoroutinesplayground.testing.BaseMockitoTest
import kotlinx.coroutines.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class AppCoroutinesHelpersTest : BaseMockitoTest() {

    private class TestException(message: String) : Exception(message)

    private val trackedEvents = mutableListOf<String>()

    @Before
    fun before() {
        trackedEvents.clear()
    }

    // region Test

    @Test
    fun `Async task exception cancels siblings`() = runBlocking {
        val job = Job()
        val coroutineScope = CoroutineScope(Dispatchers.Default + job)

        startJob(coroutineScope, coroutineScope.coroutineContext) {
            trackEvent("JOB_START")

            val deferred1 = startTaskAsync(this, Dispatchers.Default) {
                delay(100)
                trackEvent("TASK1_START")
                delay(1000)
                throw TestException("TASK1_EXCEPTION")
            }

            val deferred2 = startTaskAsync(this, Dispatchers.Default) {
                delay(1000)
                trackEvent("TASK2_START")
                delay(2000)
                trackEvent("TASK2_END")
            }

            try {
                awaitAllOrCancel(deferred1, deferred2)
            } catch (e: TestException) {
                trackEvent(e.message!!)
            }

            trackEvent("JOB_END")
        }

        job.children.forEach { it.join() }

        assertThatEventsSequenceIs(
            "JOB_START",
            "TASK1_START",
            "TASK2_START",
            "TASK1_EXCEPTION",
            "JOB_END"
        )
    }

    @Test
    fun `Async task exception doesn't cancel siblings`() = runBlocking {
        val job = Job()
        val coroutineScope = CoroutineScope(Dispatchers.Default + job)

        startJob(coroutineScope, coroutineScope.coroutineContext) {
            trackEvent("JOB_START")

            val deferred1 = startTaskAsync(this, Dispatchers.Default) {
                delay(100)
                trackEvent("TASK1_START")
                delay(1000)
                throw TestException("TASK1_EXCEPTION")
            }

            val deferred2 = startTaskAsync(this, Dispatchers.Default) {
                delay(1000)
                trackEvent("TASK2_START")
                delay(2000)
                trackEvent("TASK2_END")
            }

            try {
                deferred1.await()
            } catch (e: TestException) {
                trackEvent(e.message!!)
            }

            deferred2.await()

            trackEvent("JOB_END")
        }

        job.children.forEach { it.join() }

        assertThatEventsSequenceIs(
            "JOB_START",
            "TASK1_START",
            "TASK2_START",
            "TASK1_EXCEPTION",
            "TASK2_END",
            "JOB_END"
        )
    }

    @Test
    fun `Async tasks cancelled correctly`() = runBlocking {
        val job = Job()
        val coroutineScope = CoroutineScope(Dispatchers.Default + job)

        startJob(coroutineScope, coroutineScope.coroutineContext) {
            trackEvent("JOB_START")

            val deferred1 = startTaskAsync(this, Dispatchers.Default) {
                delay(100)
                trackEvent("TASK1_START")
                delay(2000)
                trackEvent("TASK1_END")
            }

            val deferred2 = startTaskAsync(this, Dispatchers.Default) {
                delay(500)
                trackEvent("TASK2_START")
                delay(2000)
                trackEvent("TASK2_END")
            }

            deferred1.await()
            deferred2.await()

            trackEvent("JOB_END")
        }

        startJob(testAppCoroutineScope, testAppCoroutineScope.coroutineContext) {
            delay(1000)
            job.cancelChildren()
        }

        job.children.forEach { it.join() }

        assertThatEventsSequenceIs(
            "JOB_START",
            "TASK1_START",
            "TASK2_START"
        )
    }

    @Test
    fun `Nested tasks cancelled correctly`() = runBlocking {
        val job = Job()
        val coroutineScope = CoroutineScope(Dispatchers.Default + job)

        startJob(coroutineScope, coroutineScope.coroutineContext) {
            trackEvent("JOB_START")

            val deferred1 = startTaskAsync(this, Dispatchers.Default) {
                delay(100)
                trackEvent("TASK1_START")

                val nestedDeferred11 = startTaskAsync(this, Dispatchers.Default) {
                    delay(100)
                    trackEvent("TASK1-1_START")
                    delay(2000)
                    trackEvent("TASK1-1_END")
                }

                startTask(Dispatchers.IO) {
                    val nestedDeferred12 = startTaskAsync(this, Dispatchers.Default) {
                        delay(300)
                        trackEvent("TASK1-2_START")
                        delay(2000)
                        trackEvent("TASK1-2_END")
                    }

                    nestedDeferred12.await()
                }

                nestedDeferred11.await()

                delay(2000)
                trackEvent("TASK1_END")
            }

            val deferred2 = startTaskAsync(this, Dispatchers.Default) {
                delay(700)
                trackEvent("TASK2_START")

                val nestedDeferred21 = startTaskAsync(this, Dispatchers.Default) {
                    delay(100)
                    trackEvent("TASK2-1_START")
                    delay(2000)
                    trackEvent("TASK2-1_END")
                }

                startTask(Dispatchers.IO) {
                    val nestedDeferred22 = startTaskAsync(this, Dispatchers.Default) {
                        delay(300)
                        trackEvent("TASK2-2_START")
                        delay(2000)
                        trackEvent("TASK2-2_END")
                    }

                    nestedDeferred22.await()
                }

                nestedDeferred21.await()

                delay(2000)
                trackEvent("TASK2_END")
            }

            deferred1.await()
            deferred2.await()

            trackEvent("JOB_END")
        }

        startJob(testAppCoroutineScope, testAppCoroutineScope.coroutineContext) {
            delay(1500)
            job.cancelChildren()
        }

        job.children.forEach { it.join() }

        assertThatEventsSequenceIs(
            "JOB_START",
            "TASK1_START",
            "TASK1-1_START",
            "TASK1-2_START",
            "TASK2_START",
            "TASK2-1_START",
            "TASK2-2_START"
        )
    }

    @Test
    fun `Exception propagated correctly in nested tasks`() = runBlocking {
        val job = Job()
        val coroutineScope = CoroutineScope(Dispatchers.Default + job)

        startJob(coroutineScope, coroutineScope.coroutineContext) {
            trackEvent("JOB_START")

            val deferred1 = startTaskAsync(this, Dispatchers.Default) {
                delay(100)
                trackEvent("TASK1_START")

                startTask(Dispatchers.IO) {
                    val nestedDeferred11 = startTaskAsync(this, Dispatchers.Default) {
                        delay(300)
                        trackEvent("TASK1-1_START")
                        delay(400)
                        throw TestException("TASK1-1_EXCEPTION")
                    }

                    nestedDeferred11.await()
                }
            }

            val deferred2 = startTaskAsync(this, Dispatchers.Default) {
                delay(500)
                trackEvent("TASK2_START")

                val nestedDeferred21 = startTaskAsync(this, Dispatchers.Default) {
                    delay(100)
                    trackEvent("TASK2-1_START")
                    delay(1000)
                    throw TestException("TASK2-1_EXCEPTION")
                }

                val nestedDeferred22 = startTaskAsync(this, Dispatchers.Default) {
                    delay(500)
                    trackEvent("TASK2-2_START")
                    delay(2000)
                    trackEvent("TASK2-2_END")
                }

                nestedDeferred22.await()
                nestedDeferred21.await()
            }

            val deferred3 = startTaskAsync(this, Dispatchers.Default) {
                delay(3500)
                trackEvent("TASK3_START")

                val nestedDeferred31 = startTaskAsync(this, Dispatchers.Default) {
                    delay(100)
                    trackEvent("TASK3-1_START")
                    delay(700)
                    throw TestException("TASK3-1_EXCEPTION")
                }

                val nestedDeferred32 = startTaskAsync(this, Dispatchers.Default) {
                    delay(300)
                    trackEvent("TASK3-2_START")
                    delay(2000)
                    trackEvent("TASK3-2_END")
                }

                try {
                    awaitAllOrCancel(nestedDeferred31, nestedDeferred32)
                } catch (e: TestException) {
                    trackEvent(e.message!!)
                }
            }

            try {
                deferred1.await()
            } catch (e: TestException) {
                trackEvent(e.message!!)
            }

            try {
                deferred2.await()
            } catch (e: TestException) {
                trackEvent(e.message!!)
            }

            deferred3.await()

            trackEvent("JOB_END")
        }

        job.children.forEach { it.join() }

        assertThatEventsSequenceIs(
            "JOB_START",
            "TASK1_START",
            "TASK1-1_START",
            "TASK2_START",
            "TASK2-1_START",
            "TASK1-1_EXCEPTION",
            "TASK2-2_START",
            "TASK2-2_END",
            "TASK2-1_EXCEPTION",
            "TASK3_START",
            "TASK3-1_START",
            "TASK3-2_START",
            "TASK3-1_EXCEPTION",
            "JOB_END"
        )
    }

    // endregion Test

    // region Helper

    private fun trackEvent(event: String) {
        trackedEvents.add(event)
    }

    private fun assertThatEventsSequenceIs(vararg expectedEventsSequence: String) {
        assertThat(trackedEvents).isEqualTo(expectedEventsSequence.asList())
    }

    // endregion Helper
}