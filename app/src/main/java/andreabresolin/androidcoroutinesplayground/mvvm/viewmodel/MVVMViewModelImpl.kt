package andreabresolin.androidcoroutinesplayground.mvvm.viewmodel

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutineScope
import andreabresolin.androidcoroutinesplayground.app.domain.task.*
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionError
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionState
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionState.*
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Deferred

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
    private val callbackTask3: CallbackTaskUseCase
) : MVVMViewModel(appCoroutineScope) {

    override val task1State = MutableLiveData<TaskExecutionState>()
    override val task2State = MutableLiveData<TaskExecutionState>()
    override val task3State = MutableLiveData<TaskExecutionState>()

    private fun processTaskResult(taskExecutionResult: TaskExecutionResult): TaskExecutionState {
        return when (taskExecutionResult) {
            is TaskExecutionSuccess -> COMPLETED
            is TaskExecutionError -> ERROR
        }
    }

    override fun runSequentialTasks() = uiCoroutine {
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

    override fun runParallelTasks() = uiCoroutine {
        task1State.value = INITIAL
        task2State.value = INITIAL
        task3State.value = INITIAL

        delayTask(1000)

        task1State.value = RUNNING
        val task1Result: Deferred<TaskExecutionResult> = parallelTask1.executeAsync(100, 500, 1500)

        task2State.value = RUNNING
        val task2Result: Deferred<TaskExecutionResult> = parallelTask2.executeAsync(300, 200, 2000)

        task3State.value = RUNNING
        val task3Result: Deferred<TaskExecutionResult> = parallelTask3.executeAsync(200, 600, 1800)

        task1State.value = processTaskResult(task1Result.await())
        task2State.value = processTaskResult(task2Result.await())
        task3State.value = processTaskResult(task3Result.await())
    }

    override fun runSequentialTasksWithError() = uiCoroutine {
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

    override fun runParallelTasksWithError() = uiCoroutine {
        task1State.value = INITIAL
        task2State.value = INITIAL
        task3State.value = INITIAL

        delayTask(1000)

        task1State.value = RUNNING
        val task1Result: Deferred<TaskExecutionResult> = parallelTask1.executeAsync(100, 500, 1500)

        task2State.value = RUNNING
        val task2Result: Deferred<TaskExecutionResult> = parallelErrorTask.executeAsync(300, 200, 2000)

        task3State.value = RUNNING
        val task3Result: Deferred<TaskExecutionResult> = parallelTask3.executeAsync(200, 600, 1800)

        task1State.value = processTaskResult(task1Result.await())
        task2State.value = processTaskResult(task2Result.await())
        task3State.value = processTaskResult(task3Result.await())
    }

    override fun runMultipleTasks() = uiCoroutine {
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

    override fun runCallbackTasksWithError() = uiCoroutine {
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
}