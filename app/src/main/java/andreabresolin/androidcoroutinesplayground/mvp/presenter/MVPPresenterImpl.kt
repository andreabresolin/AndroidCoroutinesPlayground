package andreabresolin.androidcoroutinesplayground.mvp.presenter

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutineScope
import andreabresolin.androidcoroutinesplayground.app.domain.task.*
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionError
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionState
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionState.*
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.app.presentation.BasePresenter
import andreabresolin.androidcoroutinesplayground.mvp.view.MVPView
import kotlinx.coroutines.Deferred
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
    private val callbackTask3: CallbackTaskUseCase
) : BasePresenter(appCoroutineScope), MVPPresenter {

    private fun processTaskResult(taskExecutionResult: TaskExecutionResult): TaskExecutionState {
        return when (taskExecutionResult) {
            is TaskExecutionSuccess -> COMPLETED
            is TaskExecutionError -> ERROR
        }
    }

    override fun runSequentialTasks() = uiTask {
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

    override fun runParallelTasks() = uiTask {
        view.updateTaskExecutionState(1, INITIAL)
        view.updateTaskExecutionState(2, INITIAL)
        view.updateTaskExecutionState(3, INITIAL)

        delayTask(1000)

        view.updateTaskExecutionState(1, RUNNING)
        val task1Result: Deferred<TaskExecutionResult> = parallelTask1.execute(100, 500, 1500)

        view.updateTaskExecutionState(2, RUNNING)
        val task2Result: Deferred<TaskExecutionResult> = parallelTask2.execute(300, 200, 2000)

        view.updateTaskExecutionState(3, RUNNING)
        val task3Result: Deferred<TaskExecutionResult> = parallelTask3.execute(200, 600, 1800)

        view.updateTaskExecutionState(1, processTaskResult(task1Result.await()))
        view.updateTaskExecutionState(2, processTaskResult(task2Result.await()))
        view.updateTaskExecutionState(3, processTaskResult(task3Result.await()))
    }

    override fun runSequentialTasksWithError() = uiTask {
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

    override fun runParallelTasksWithError() = uiTask {
        view.updateTaskExecutionState(1, INITIAL)
        view.updateTaskExecutionState(2, INITIAL)
        view.updateTaskExecutionState(3, INITIAL)

        delayTask(1000)

        view.updateTaskExecutionState(1, RUNNING)
        val task1Result: Deferred<TaskExecutionResult> = parallelTask1.execute(100, 500, 1500)

        view.updateTaskExecutionState(2, RUNNING)
        val task2Result: Deferred<TaskExecutionResult> = parallelErrorTask.execute(300, 200, 2000)

        view.updateTaskExecutionState(3, RUNNING)
        val task3Result: Deferred<TaskExecutionResult> = parallelTask3.execute(200, 600, 1800)

        view.updateTaskExecutionState(1, processTaskResult(task1Result.await()))
        view.updateTaskExecutionState(2, processTaskResult(task2Result.await()))
        view.updateTaskExecutionState(3, processTaskResult(task3Result.await()))
    }

    override fun runMultipleTasks() = uiTask {
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

    override fun runCallbackTasks() = uiTask {
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
}