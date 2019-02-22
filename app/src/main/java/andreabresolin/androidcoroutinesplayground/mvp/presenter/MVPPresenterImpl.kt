package andreabresolin.androidcoroutinesplayground.mvp.presenter

import andreabresolin.androidcoroutinesplayground.app.coroutines.*
import andreabresolin.androidcoroutinesplayground.app.domain.task.*
import andreabresolin.androidcoroutinesplayground.app.exception.CustomTaskException
import andreabresolin.androidcoroutinesplayground.app.model.*
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionState.*
import andreabresolin.androidcoroutinesplayground.app.presentation.BasePresenter
import andreabresolin.androidcoroutinesplayground.mvp.view.MVPView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.Channel
import java.io.IOException
import javax.inject.Inject

class MVPPresenterImpl
@Inject constructor(
    appCoroutineScope: AppCoroutineScope,
    private val view: MVPView,
    private val sequentialTask1: SequentialTaskUseCase,
    private val sequentialTask2: SequentialTaskUseCase,
    private val sequentialTask3: SequentialTaskUseCase,
    private val parallelTask1: ParallelTaskUseCase,
    private val parallelTask2: ParallelTaskUseCase,
    private val parallelTask3: ParallelTaskUseCase,
    private val sequentialErrorTask: SequentialErrorTaskUseCase,
    private val parallelErrorTask: ParallelErrorTaskUseCase,
    private val multipleTasks1: MultipleTasksUseCase,
    private val multipleTasks2: MultipleTasksUseCase,
    private val multipleTasks3: MultipleTasksUseCase,
    private val callbackTask1: CallbackTaskUseCase,
    private val callbackTask2: CallbackTaskUseCase,
    private val callbackTask3: CallbackTaskUseCase,
    private val longComputationTask1: LongComputationTaskUseCase,
    private val longComputationTask2: LongComputationTaskUseCase,
    private val longComputationTask3: LongComputationTaskUseCase,
    private val channelTask1: ChannelTaskUseCase,
    private val channelTask2: ChannelTaskUseCase,
    private val channelTask3: ChannelTaskUseCase,
    private val exceptionsTask: ExceptionsTaskUseCase
) : BasePresenter(appCoroutineScope), MVPPresenter {

    private var longComputationTask1Deferred: Deferred<TaskExecutionResult>? = null
    private var longComputationTask2Deferred: Deferred<TaskExecutionResult>? = null
    private var longComputationTask3Deferred: Deferred<TaskExecutionResult>? = null

    private fun processTaskResult(taskExecutionResult: TaskExecutionResult): TaskExecutionState {
        return when (taskExecutionResult) {
            is TaskExecutionSuccess -> COMPLETED
            is TaskExecutionCancelled -> CANCELLED
            is TaskExecutionError -> ERROR
        }
    }

    override fun runSequentialTasks() = uiJob {
        view.updateTaskExecutionState(1, INITIAL)
        view.updateTaskExecutionState(2, INITIAL)
        view.updateTaskExecutionState(3, INITIAL)

        delayTask(1000)

        view.updateTaskExecutionState(1, RUNNING)
        val task1Result: TaskExecutionResult = sequentialTask1.execute(100, 500, 1500)
        view.updateTaskExecutionState(1, processTaskResult(task1Result))

        view.updateTaskExecutionState(2, RUNNING)
        val task2Result: TaskExecutionResult = sequentialTask2.execute(300, 200, 2000)
        view.updateTaskExecutionState(2, processTaskResult(task2Result))

        view.updateTaskExecutionState(3, RUNNING)
        val task3Result: TaskExecutionResult = sequentialTask3.execute(200, 600, 1800)
        view.updateTaskExecutionState(3, processTaskResult(task3Result))
    }

    override fun runParallelTasks() = uiJob {
        view.updateTaskExecutionState(1, INITIAL)
        view.updateTaskExecutionState(2, INITIAL)
        view.updateTaskExecutionState(3, INITIAL)

        delayTask(1000)

        view.updateTaskExecutionState(1, RUNNING)
        val task1Result: Deferred<TaskExecutionResult> = parallelTask1.executeAsync(this, 100, 500, 1500)

        view.updateTaskExecutionState(2, RUNNING)
        val task2Result: Deferred<TaskExecutionResult> = parallelTask2.executeAsync(this, 300, 200, 2000)

        view.updateTaskExecutionState(3, RUNNING)
        val task3Result: Deferred<TaskExecutionResult> = parallelTask3.executeAsync(this, 200, 600, 1800)

        view.updateTaskExecutionState(1, processTaskResult(task1Result.await()))
        view.updateTaskExecutionState(2, processTaskResult(task2Result.await()))
        view.updateTaskExecutionState(3, processTaskResult(task3Result.await()))
    }

    override fun runSequentialTasksWithError() = uiJob {
        view.updateTaskExecutionState(1, INITIAL)
        view.updateTaskExecutionState(2, INITIAL)
        view.updateTaskExecutionState(3, INITIAL)

        delayTask(1000)

        view.updateTaskExecutionState(1, RUNNING)
        val task1Result: TaskExecutionResult = sequentialTask1.execute(100, 500, 1500)
        view.updateTaskExecutionState(1, processTaskResult(task1Result))

        view.updateTaskExecutionState(2, RUNNING)
        val task2Result: TaskExecutionResult = sequentialErrorTask.execute(300, 200, 2000)
        view.updateTaskExecutionState(2, processTaskResult(task2Result))

        view.updateTaskExecutionState(3, RUNNING)
        val task3Result: TaskExecutionResult = sequentialTask3.execute(200, 600, 1800)
        view.updateTaskExecutionState(3, processTaskResult(task3Result))
    }

    override fun runParallelTasksWithError() = uiJob {
        view.updateTaskExecutionState(1, INITIAL)
        view.updateTaskExecutionState(2, INITIAL)
        view.updateTaskExecutionState(3, INITIAL)

        delayTask(1000)

        view.updateTaskExecutionState(1, RUNNING)
        val task1Result: Deferred<TaskExecutionResult> = parallelTask1.executeAsync(this, 100, 500, 1500)

        view.updateTaskExecutionState(2, RUNNING)
        val task2Result: Deferred<TaskExecutionResult> = parallelErrorTask.executeAsync(this, 300, 200, 2000)

        view.updateTaskExecutionState(3, RUNNING)
        val task3Result: Deferred<TaskExecutionResult> = parallelTask3.executeAsync(this, 200, 600, 1800)

        view.updateTaskExecutionState(1, processTaskResult(task1Result.await()))
        view.updateTaskExecutionState(2, processTaskResult(task2Result.await()))
        view.updateTaskExecutionState(3, processTaskResult(task3Result.await()))
    }

    override fun runMultipleTasks() = uiJob {
        view.updateTaskExecutionState(1, INITIAL)
        view.updateTaskExecutionState(2, INITIAL)
        view.updateTaskExecutionState(3, INITIAL)

        delayTask(1000)

        view.updateTaskExecutionState(1, RUNNING)
        val task1Result: TaskExecutionResult = multipleTasks1.execute(1, 10, 100)
        view.updateTaskExecutionState(1, processTaskResult(task1Result))

        view.updateTaskExecutionState(2, RUNNING)
        val task2Result: TaskExecutionResult = multipleTasks2.execute(2, 20, 200)
        view.updateTaskExecutionState(2, processTaskResult(task2Result))

        view.updateTaskExecutionState(3, RUNNING)
        val task3Result: TaskExecutionResult = multipleTasks3.execute(3, 30, 300)
        view.updateTaskExecutionState(3, processTaskResult(task3Result))
    }

    override fun runCallbackTasksWithError() = uiJob {
        view.updateTaskExecutionState(1, INITIAL)
        view.updateTaskExecutionState(2, INITIAL)
        view.updateTaskExecutionState(3, INITIAL)

        delayTask(1000)

        view.updateTaskExecutionState(1, RUNNING)
        val task1Result: TaskExecutionResult = callbackTask1.execute("RANDOM STRING")
        view.updateTaskExecutionState(1, processTaskResult(task1Result))

        view.updateTaskExecutionState(2, RUNNING)
        val task2Result: TaskExecutionResult = callbackTask2.execute("SUCCESS")
        view.updateTaskExecutionState(2, processTaskResult(task2Result))

        view.updateTaskExecutionState(3, RUNNING)
        val task3Result: TaskExecutionResult = callbackTask3.execute("SUCCESS")
        view.updateTaskExecutionState(3, processTaskResult(task3Result))
    }

    override fun runLongComputationTasks() {
        uiJob {
            view.updateTaskExecutionState(1, INITIAL)
            delayTask(1000)
            view.updateTaskExecutionState(1, RUNNING)

            longComputationTask1Deferred = longComputationTask1.executeAsync(this, 500, 10)
            longComputationTask1Deferred?.let {
                view.updateTaskExecutionState(1, processTaskResult(it.awaitOrReturn(TaskExecutionCancelled)))
            }
        }

        uiJob {
            view.updateTaskExecutionState(2, INITIAL)
            delayTask(1000)
            view.updateTaskExecutionState(2, RUNNING)

            longComputationTask2Deferred = longComputationTask2.executeAsync(this, 1000, 5)
            longComputationTask2Deferred?.let {
                view.updateTaskExecutionState(2, processTaskResult(it.awaitOrReturn(TaskExecutionCancelled)))
            }
        }

        uiJob {
            view.updateTaskExecutionState(3, INITIAL)
            delayTask(1000)
            view.updateTaskExecutionState(3, RUNNING)

            longComputationTask3Deferred = longComputationTask3.executeAsync(this, 300, 20)
            longComputationTask3Deferred?.let {
                view.updateTaskExecutionState(3, processTaskResult(it.awaitOrReturn(TaskExecutionCancelled)))
            }
        }
    }

    override fun cancelLongComputationTask1() {
        longComputationTask1Deferred?.cancel()
    }

    override fun cancelLongComputationTask2() {
        longComputationTask2Deferred?.cancel()
    }

    override fun cancelLongComputationTask3() {
        longComputationTask3Deferred?.cancel()
    }

    override fun runLongComputationTasksWithTimeout() {
        uiJob {
            view.updateTaskExecutionState(1, INITIAL)
            delayTask(1000)
            view.updateTaskExecutionState(1, RUNNING)

            val taskResult: Deferred<TaskExecutionResult> = longComputationTask1.executeAsync(this, 500, 10, 4000)
            view.updateTaskExecutionState(1, processTaskResult(taskResult.awaitOrReturn(TaskExecutionCancelled)))
        }

        uiJob {
            view.updateTaskExecutionState(2, INITIAL)
            delayTask(1000)
            view.updateTaskExecutionState(2, RUNNING)

            try {
                uiTask(timeout = 3000) {
                    val taskResult: Deferred<TaskExecutionResult> = longComputationTask2.executeAsync(this, 1000, 5)
                    view.updateTaskExecutionState(2, processTaskResult(taskResult.await()))
                }
            } catch (e: TimeoutCancellationException) {
                view.updateTaskExecutionState(2, processTaskResult(TaskExecutionCancelled))
            }
        }

        uiJob(timeout = 2000) {
            view.updateTaskExecutionState(3, INITIAL)
            delayTask(1000)
            view.updateTaskExecutionState(3, RUNNING)

            val taskResult: Deferred<TaskExecutionResult> = longComputationTask3.executeAsync(this, 300, 20)
            view.updateTaskExecutionState(3, processTaskResult(taskResult.awaitOrReturn(TaskExecutionCancelled)))
        }
    }

    override fun runChannelsTasks() {
        uiJob {
            view.updateTaskExecutionState(1, INITIAL)

            val channel = Channel<Long>()
            val itemProcessingTime = 400L

            val taskResult: Deferred<TaskExecutionResult> = channelTask1.executeAsync(this, 800, 10, channel)

            for (receivedItem in channel) {
                view.updateTaskExecutionState(1, RUNNING)
                backgroundTask { delayTask(itemProcessingTime) }
                view.updateTaskExecutionState(1, INITIAL)
            }

            view.updateTaskExecutionState(1, processTaskResult(taskResult.await()))
        }

        uiJob {
            try {
                view.updateTaskExecutionState(2, INITIAL)

                val channel = Channel<Long>()
                val itemProcessingTime = 1000L

                val taskResult: Deferred<TaskExecutionResult> = channelTask2.executeAsync(this, 800, 10, channel)

                for (receivedItem in channel) {
                    view.updateTaskExecutionState(2, RUNNING)
                    backgroundTask { delayTask(itemProcessingTime) }
                    view.updateTaskExecutionState(2, INITIAL)
                }

                view.updateTaskExecutionState(2, processTaskResult(taskResult.await()))
            } catch (e: CancellationException) {
                view.updateTaskExecutionState(2, processTaskResult(TaskExecutionCancelled))
            }
        }

        uiJob {
            view.updateTaskExecutionState(3, INITIAL)

            val primaryChannel = Channel<Long>()
            val backpressureChannel = Channel<Long>()
            val itemProcessingTime = 1500L

            val taskResult: Deferred<TaskExecutionResult> = channelTask3.executeAsync(this, 500, 20, primaryChannel, backpressureChannel)

            val primaryHandler = backgroundTaskAsync {
                for (receivedItem in primaryChannel) {
                    uiTask { view.updateTaskExecutionState(3, RUNNING) }
                    delayTask(itemProcessingTime)
                    uiTask { view.updateTaskExecutionState(3, INITIAL) }
                }
            }

            val backpressureHandler = backgroundTaskAsync {
                for (receivedItem in backpressureChannel) {
                    uiTask { view.updateTaskExecutionState(3, ERROR) }
                }
            }

            primaryHandler.await()
            backpressureHandler.await()
            view.updateTaskExecutionState(3, processTaskResult(taskResult.await()))
        }
    }

    override fun runExceptionsTasks() = uiJob {
        view.updateTaskExecutionState(1, INITIAL)
        view.updateTaskExecutionState(2, INITIAL)
        view.updateTaskExecutionState(3, INITIAL)

        delayTask(1000)

        view.updateTaskExecutionState(1, RUNNING)
        try {
            val task1Result: TaskExecutionResult = exceptionsTask.execute(100, 500, 1500)
            view.updateTaskExecutionState(1, processTaskResult(task1Result))
        } catch (e: CustomTaskException) {
            view.updateTaskExecutionState(1, ERROR)
        }

        view.updateTaskExecutionState(2, RUNNING)
        val task2Result: Deferred<TaskExecutionResult> = exceptionsTask.executeAsync(this, 300, 200, 2000)

        view.updateTaskExecutionState(3, RUNNING)
        val task3Result: Deferred<TaskExecutionResult> = exceptionsTask.executeWithRepositoryAsync(this, 200, 600, 1800)

        try {
            view.updateTaskExecutionState(2, processTaskResult(task2Result.await()))
        } catch (e: CustomTaskException) {
            view.updateTaskExecutionState(2, ERROR)
        }

        try {
            view.updateTaskExecutionState(3, processTaskResult(task3Result.await()))
        } catch (e: IOException) {
            view.updateTaskExecutionState(3, ERROR)
        }
    }
}