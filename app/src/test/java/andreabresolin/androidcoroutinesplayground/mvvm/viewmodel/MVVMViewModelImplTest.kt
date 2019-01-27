package andreabresolin.androidcoroutinesplayground.mvvm.viewmodel

import andreabresolin.androidcoroutinesplayground.app.domain.task.*
import andreabresolin.androidcoroutinesplayground.app.exception.CustomTaskException
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionError
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionState
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionState.*
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.base.BaseViewModelTest
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.anyLong
import org.mockito.BDDMockito.given
import org.mockito.Mock

class MVVMViewModelImplTest : BaseViewModelTest() {

    @Mock
    private lateinit var mockSequentialTask1: SequentialTaskUseCase
    @Mock
    private lateinit var mockSequentialTask2: SequentialTaskUseCase
    @Mock
    private lateinit var mockSequentialTask3: SequentialTaskUseCase
    @Mock
    private lateinit var mockParallelTask1: ParallelTaskUseCase
    @Mock
    private lateinit var mockParallelTask2: ParallelTaskUseCase
    @Mock
    private lateinit var mockParallelTask3: ParallelTaskUseCase
    @Mock
    private lateinit var mockSequentialErrorTask: SequentialErrorTaskUseCase
    @Mock
    private lateinit var mockParallelErrorTask: ParallelErrorTaskUseCase
    @Mock
    private lateinit var mockMultipleTasks1: MultipleTasksUseCase
    @Mock
    private lateinit var mockMultipleTasks2: MultipleTasksUseCase
    @Mock
    private lateinit var mockMultipleTasks3: MultipleTasksUseCase
    @Mock
    private lateinit var mockCallbackTask1: CallbackTaskUseCase
    @Mock
    private lateinit var mockCallbackTask2: CallbackTaskUseCase
    @Mock
    private lateinit var mockCallbackTask3: CallbackTaskUseCase

    private lateinit var subject: MVVMViewModelImpl

    @Before
    fun before() {
        subject = MVVMViewModelImpl(
            appCoroutineScope,
            mockSequentialTask1,
            mockSequentialTask2,
            mockSequentialTask3,
            mockParallelTask1,
            mockParallelTask2,
            mockParallelTask3,
            mockSequentialErrorTask,
            mockParallelErrorTask,
            mockMultipleTasks1,
            mockMultipleTasks2,
            mockMultipleTasks3,
            mockCallbackTask1,
            mockCallbackTask2,
            mockCallbackTask3)
    }

    // region Test

    @Test
    fun runSequentialTasks_runsSequentialTasksWithoutError() {
        givenThatStateWillChange(subject.task1State)
        givenThatStateWillChange(subject.task2State)
        givenThatStateWillChange(subject.task3State)
        givenThatSequentialTaskWillReturn(mockSequentialTask1, TaskExecutionSuccess(10))
        givenThatSequentialTaskWillReturn(mockSequentialTask2, TaskExecutionSuccess(20))
        givenThatSequentialTaskWillReturn(mockSequentialTask3, TaskExecutionSuccess(30))
        whenRunSequentialTasks()
        thenTaskStateChangesAre(subject.task1State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStateChangesAre(subject.task2State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStateChangesAre(subject.task3State, listOf(INITIAL, RUNNING, COMPLETED))
    }

    @Test
    fun runParallelTasks_runsParallelTasksWithoutError() {
        givenThatStateWillChange(subject.task1State)
        givenThatStateWillChange(subject.task2State)
        givenThatStateWillChange(subject.task3State)
        givenThatParallelTaskWillReturn(mockParallelTask1, TaskExecutionSuccess(10))
        givenThatParallelTaskWillReturn(mockParallelTask2, TaskExecutionSuccess(20))
        givenThatParallelTaskWillReturn(mockParallelTask3, TaskExecutionSuccess(30))
        whenRunParallelTasks()
        thenTaskStateChangesAre(subject.task1State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStateChangesAre(subject.task2State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStateChangesAre(subject.task3State, listOf(INITIAL, RUNNING, COMPLETED))
    }

    @Test
    fun runSequentialTasksWithError_runsSequentialTasksWithError() {
        givenThatStateWillChange(subject.task1State)
        givenThatStateWillChange(subject.task2State)
        givenThatStateWillChange(subject.task3State)
        givenThatSequentialTaskWillReturn(mockSequentialTask1, TaskExecutionSuccess(10))
        givenThatSequentialErrorTaskWillReturn(mockSequentialErrorTask, TaskExecutionError(CustomTaskException()))
        givenThatSequentialTaskWillReturn(mockSequentialTask3, TaskExecutionSuccess(30))
        whenRunSequentialTasksWithError()
        thenTaskStateChangesAre(subject.task1State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStateChangesAre(subject.task2State, listOf(INITIAL, RUNNING, ERROR))
        thenTaskStateChangesAre(subject.task3State, listOf(INITIAL, RUNNING, COMPLETED))
    }

    @Test
    fun runParallelTasksWithError_runsParallelTasksWithError() {
        givenThatStateWillChange(subject.task1State)
        givenThatStateWillChange(subject.task2State)
        givenThatStateWillChange(subject.task3State)
        givenThatParallelTaskWillReturn(mockParallelTask1, TaskExecutionSuccess(10))
        givenThatParallelErrorTaskWillReturn(mockParallelErrorTask, TaskExecutionError(CustomTaskException()))
        givenThatParallelTaskWillReturn(mockParallelTask3, TaskExecutionSuccess(30))
        whenRunParallelTasksWithError()
        thenTaskStateChangesAre(subject.task1State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStateChangesAre(subject.task2State, listOf(INITIAL, RUNNING, ERROR))
        thenTaskStateChangesAre(subject.task3State, listOf(INITIAL, RUNNING, COMPLETED))
    }

    @Test
    fun runMultipleTasks_runsMultipleTasksWithoutError() {
        givenThatStateWillChange(subject.task1State)
        givenThatStateWillChange(subject.task2State)
        givenThatStateWillChange(subject.task3State)
        givenThatMultipleTasksWillReturn(mockMultipleTasks1, TaskExecutionSuccess(10))
        givenThatMultipleTasksWillReturn(mockMultipleTasks2, TaskExecutionSuccess(20))
        givenThatMultipleTasksWillReturn(mockMultipleTasks3, TaskExecutionSuccess(30))
        whenRunMultipleTasks()
        thenTaskStateChangesAre(subject.task1State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStateChangesAre(subject.task2State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStateChangesAre(subject.task3State, listOf(INITIAL, RUNNING, COMPLETED))
    }

    @Test
    fun runCallbackTasks_runsCallbackTasksWithError() {
        givenThatStateWillChange(subject.task1State)
        givenThatStateWillChange(subject.task2State)
        givenThatStateWillChange(subject.task3State)
        givenThatCallbackTaskWillReturn(mockCallbackTask1, TaskExecutionSuccess(10))
        givenThatCallbackTaskWillReturn(mockCallbackTask2, TaskExecutionError(CustomTaskException()))
        givenThatCallbackTaskWillReturn(mockCallbackTask3, TaskExecutionError(CustomTaskException()))
        whenRunCallbackTasks()
        thenTaskStateChangesAre(subject.task1State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStateChangesAre(subject.task2State, listOf(INITIAL, RUNNING, ERROR))
        thenTaskStateChangesAre(subject.task3State, listOf(INITIAL, RUNNING, ERROR))
    }

    // endregion Test

    // region Given

    private fun <T> givenThatStateWillChange(liveData: LiveData<T>) {
        trackLiveDataChanges(liveData)
    }

    private fun givenThatSequentialTaskWillReturn(sequentialTask: SequentialTaskUseCase,
                                                  taskExecutionResult: TaskExecutionResult) = runBlocking {
        given(sequentialTask.execute(anyLong(), anyLong(), anyLong())).willReturn(taskExecutionResult)
    }

    private fun givenThatParallelTaskWillReturn(parallelTask: ParallelTaskUseCase,
                                                taskExecutionResult: TaskExecutionResult) = runBlocking {
        given(parallelTask.execute(anyLong(), anyLong(), anyLong())).willReturn(CompletableDeferred(taskExecutionResult))
    }

    private fun givenThatSequentialErrorTaskWillReturn(sequentialErrorTask: SequentialErrorTaskUseCase,
                                                       taskExecutionResult: TaskExecutionResult) = runBlocking {
        given(sequentialErrorTask.execute(anyLong(), anyLong(), anyLong())).willReturn(taskExecutionResult)
    }

    private fun givenThatParallelErrorTaskWillReturn(parallelErrorTask: ParallelErrorTaskUseCase,
                                                     taskExecutionResult: TaskExecutionResult) = runBlocking {
        given(parallelErrorTask.execute(anyLong(), anyLong(), anyLong())).willReturn(CompletableDeferred(taskExecutionResult))
    }

    private fun givenThatMultipleTasksWillReturn(multipleTasks: MultipleTasksUseCase,
                                                 taskExecutionResult: TaskExecutionResult) = runBlocking {
        given(multipleTasks.execute(anyLong(), anyLong(), anyLong())).willReturn(taskExecutionResult)
    }

    private fun givenThatCallbackTaskWillReturn(callbackTask: CallbackTaskUseCase,
                                                taskExecutionResult: TaskExecutionResult) = runBlocking {
        given(callbackTask.execute(anyString())).willReturn(taskExecutionResult)
    }

    // endregion Given

    // region When

    private fun whenRunSequentialTasks() {
        subject.runSequentialTasks()
    }

    private fun whenRunParallelTasks() {
        subject.runParallelTasks()
    }

    private fun whenRunSequentialTasksWithError() {
        subject.runSequentialTasksWithError()
    }

    private fun whenRunParallelTasksWithError() {
        subject.runParallelTasksWithError()
    }

    private fun whenRunMultipleTasks() {
        subject.runMultipleTasks()
    }

    private fun whenRunCallbackTasks() {
        subject.runCallbackTasks()
    }

    // endregion When

    // region Then

    private fun thenTaskStateChangesAre(taskState: LiveData<TaskExecutionState>, states: List<TaskExecutionState>) {
        assertThatLiveDataStateChangesAre(taskState, states)
    }

    // endregion Then
}