package andreabresolin.androidcoroutinesplayground.mvvm.viewmodel

import andreabresolin.androidcoroutinesplayground.app.coroutines.*
import andreabresolin.androidcoroutinesplayground.app.domain.task.*
import andreabresolin.androidcoroutinesplayground.app.model.*
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionState.*
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.TimeoutCancellationException

class MVVMViewModelImpl
constructor(
    appCoroutineScope: AppCoroutineScope,
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
    private val longComputationTask3: LongComputationTaskUseCase
) : MVVMViewModel(appCoroutineScope) {

    override val task1State = MutableLiveData<TaskExecutionState>()
    override val task2State = MutableLiveData<TaskExecutionState>()
    override val task3State = MutableLiveData<TaskExecutionState>()

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
        task1State.value = INITIAL
        task2State.value = INITIAL
        task3State.value = INITIAL

        delayTask(1000)

        task1State.value = RUNNING
        val task1Result: TaskExecutionResult = sequentialTask1.execute(100, 500, 1500)
        task1State.value = processTaskResult(task1Result)

        task2State.value = RUNNING
        val task2Result: TaskExecutionResult = sequentialTask2.execute(300, 200, 2000)
        task2State.value = processTaskResult(task2Result)

        task3State.value = RUNNING
        val task3Result: TaskExecutionResult = sequentialTask3.execute(200, 600, 1800)
        task3State.value = processTaskResult(task3Result)
    }

    override fun runParallelTasks() = uiJob {
        task1State.value = INITIAL
        task2State.value = INITIAL
        task3State.value = INITIAL

        delayTask(1000)

        task1State.value = RUNNING
        val task1Result: Deferred<TaskExecutionResult> = parallelTask1.executeAsync(this, 100, 500, 1500)

        task2State.value = RUNNING
        val task2Result: Deferred<TaskExecutionResult> = parallelTask2.executeAsync(this, 300, 200, 2000)

        task3State.value = RUNNING
        val task3Result: Deferred<TaskExecutionResult> = parallelTask3.executeAsync(this, 200, 600, 1800)

        task1State.value = processTaskResult(task1Result.await())
        task2State.value = processTaskResult(task2Result.await())
        task3State.value = processTaskResult(task3Result.await())
    }

    override fun runSequentialTasksWithError() = uiJob {
        task1State.value = INITIAL
        task2State.value = INITIAL
        task3State.value = INITIAL

        delayTask(1000)

        task1State.value = RUNNING
        val task1Result: TaskExecutionResult = sequentialTask1.execute(100, 500, 1500)
        task1State.value = processTaskResult(task1Result)

        task2State.value = RUNNING
        val task2Result: TaskExecutionResult = sequentialErrorTask.execute(300, 200, 2000)
        task2State.value = processTaskResult(task2Result)

        task3State.value = RUNNING
        val task3Result: TaskExecutionResult = sequentialTask3.execute(200, 600, 1800)
        task3State.value = processTaskResult(task3Result)
    }

    override fun runParallelTasksWithError() = uiJob {
        task1State.value = INITIAL
        task2State.value = INITIAL
        task3State.value = INITIAL

        delayTask(1000)

        task1State.value = RUNNING
        val task1Result: Deferred<TaskExecutionResult> = parallelTask1.executeAsync(this, 100, 500, 1500)

        task2State.value = RUNNING
        val task2Result: Deferred<TaskExecutionResult> = parallelErrorTask.executeAsync(this, 300, 200, 2000)

        task3State.value = RUNNING
        val task3Result: Deferred<TaskExecutionResult> = parallelTask3.executeAsync(this, 200, 600, 1800)

        task1State.value = processTaskResult(task1Result.await())
        task2State.value = processTaskResult(task2Result.await())
        task3State.value = processTaskResult(task3Result.await())
    }

    override fun runMultipleTasks() = uiJob {
        task1State.value = INITIAL
        task2State.value = INITIAL
        task3State.value = INITIAL

        delayTask(1000)

        task1State.value = RUNNING
        val task1Result: TaskExecutionResult = multipleTasks1.execute(1, 10, 100)
        task1State.value = processTaskResult(task1Result)

        task2State.value = RUNNING
        val task2Result: TaskExecutionResult = multipleTasks2.execute(2, 20, 200)
        task2State.value = processTaskResult(task2Result)

        task3State.value = RUNNING
        val task3Result: TaskExecutionResult = multipleTasks3.execute(3, 30, 300)
        task3State.value = processTaskResult(task3Result)
    }

    override fun runCallbackTasksWithError() = uiJob {
        task1State.value = INITIAL
        task2State.value = INITIAL
        task3State.value = INITIAL

        delayTask(1000)

        task1State.value = RUNNING
        val task1Result: TaskExecutionResult = callbackTask1.execute("RANDOM STRING")
        task1State.value = processTaskResult(task1Result)

        task2State.value = RUNNING
        val task2Result: TaskExecutionResult = callbackTask2.execute("SUCCESS")
        task2State.value = processTaskResult(task2Result)

        task3State.value = RUNNING
        val task3Result: TaskExecutionResult = callbackTask3.execute("SUCCESS")
        task3State.value = processTaskResult(task3Result)
    }

    override fun runLongComputationTasks() {
        uiJob {
            task1State.value = INITIAL
            delayTask(1000)
            task1State.value = RUNNING

            longComputationTask1Deferred = longComputationTask1.executeAsync(this, 500, 10)
            longComputationTask1Deferred?.let {
                task1State.value = processTaskResult(it.awaitOrReturn(TaskExecutionCancelled))
            }
        }

        uiJob {
            task2State.value = INITIAL
            delayTask(1000)
            task2State.value = RUNNING

            longComputationTask2Deferred = longComputationTask2.executeAsync(this, 1000, 5)
            longComputationTask2Deferred?.let {
                task2State.value = processTaskResult(it.awaitOrReturn(TaskExecutionCancelled))
            }
        }

        uiJob {
            task3State.value = INITIAL
            delayTask(1000)
            task3State.value = RUNNING

            longComputationTask3Deferred = longComputationTask3.executeAsync(this, 300, 20)
            longComputationTask3Deferred?.let {
                task3State.value = processTaskResult(it.awaitOrReturn(TaskExecutionCancelled))
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
            task1State.value = INITIAL
            delayTask(1000)
            task1State.value = RUNNING

            val taskResult: Deferred<TaskExecutionResult> = longComputationTask1.executeAsync(this, 500, 10, 4000)
            task1State.value = processTaskResult(taskResult.awaitOrReturn(TaskExecutionCancelled))
        }

        uiJob {
            task2State.value = INITIAL
            delayTask(1000)
            task2State.value = RUNNING

            try {
                uiTask(timeout = 3000) {
                    val taskResult: Deferred<TaskExecutionResult> = longComputationTask2.executeAsync(this, 1000, 5)
                    task2State.value = processTaskResult(taskResult.await())
                }
            } catch (e: TimeoutCancellationException) {
                task2State.value = processTaskResult(TaskExecutionCancelled)
            }
        }

        uiJob(timeout = 2000) {
            task3State.value = INITIAL
            delayTask(1000)
            task3State.value = RUNNING

            val taskResult: Deferred<TaskExecutionResult> = longComputationTask3.executeAsync(this, 300, 20)
            task3State.value = processTaskResult(taskResult.awaitOrReturn(TaskExecutionCancelled))
        }
    }
}