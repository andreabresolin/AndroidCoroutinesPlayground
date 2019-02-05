package andreabresolin.androidcoroutinesplayground.mvvm.viewmodel

import andreabresolin.androidcoroutinesplayground.app.domain.task.*
import andreabresolin.androidcoroutinesplayground.app.exception.CustomTaskException
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionError
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionState
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionState.*
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.testing.BaseViewModelTest
import andreabresolin.androidcoroutinesplayground.testing.KotlinTestUtils.Companion.anyObj
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.*
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
    @Mock
    private lateinit var mockLongComputationTask1: LongComputationTaskUseCase
    @Mock
    private lateinit var mockLongComputationTask2: LongComputationTaskUseCase
    @Mock
    private lateinit var mockLongComputationTask3: LongComputationTaskUseCase
    @Mock
    private lateinit var mockLongComputationTask1Deferred: Deferred<TaskExecutionResult>
    @Mock
    private lateinit var mockLongComputationTask2Deferred: Deferred<TaskExecutionResult>
    @Mock
    private lateinit var mockLongComputationTask3Deferred: Deferred<TaskExecutionResult>

    private lateinit var subject: MVVMViewModelImpl

    @Before
    fun before() {
        subject = MVVMViewModelImpl(
            testAppCoroutineScope,
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
            mockCallbackTask3,
            mockLongComputationTask1,
            mockLongComputationTask2,
            mockLongComputationTask3)
    }

    // region Test

    @Test
    fun runSequentialTasks_runsSequentialTasksWithoutError() {
        givenThatStateWillChangeFor(subject.task1State)
        givenThatStateWillChangeFor(subject.task2State)
        givenThatStateWillChangeFor(subject.task3State)
        givenThatSequentialTaskWillReturn(mockSequentialTask1, TaskExecutionSuccess(10))
        givenThatSequentialTaskWillReturn(mockSequentialTask2, TaskExecutionSuccess(20))
        givenThatSequentialTaskWillReturn(mockSequentialTask3, TaskExecutionSuccess(30))
        whenRunSequentialTasks()
        thenTaskStatesSequenceIs(subject.task1State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(subject.task2State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(subject.task3State, listOf(INITIAL, RUNNING, COMPLETED))
    }

    @Test
    fun runParallelTasks_runsParallelTasksWithoutError() {
        givenThatStateWillChangeFor(subject.task1State)
        givenThatStateWillChangeFor(subject.task2State)
        givenThatStateWillChangeFor(subject.task3State)
        givenThatParallelTaskWillReturn(mockParallelTask1, TaskExecutionSuccess(10))
        givenThatParallelTaskWillReturn(mockParallelTask2, TaskExecutionSuccess(20))
        givenThatParallelTaskWillReturn(mockParallelTask3, TaskExecutionSuccess(30))
        whenRunParallelTasks()
        thenTaskStatesSequenceIs(subject.task1State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(subject.task2State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(subject.task3State, listOf(INITIAL, RUNNING, COMPLETED))
    }

    @Test
    fun runSequentialTasksWithError_runsSequentialTasksWithError() {
        givenThatStateWillChangeFor(subject.task1State)
        givenThatStateWillChangeFor(subject.task2State)
        givenThatStateWillChangeFor(subject.task3State)
        givenThatSequentialTaskWillReturn(mockSequentialTask1, TaskExecutionSuccess(10))
        givenThatSequentialErrorTaskWillReturn(mockSequentialErrorTask, TaskExecutionError(CustomTaskException()))
        givenThatSequentialTaskWillReturn(mockSequentialTask3, TaskExecutionSuccess(30))
        whenRunSequentialTasksWithError()
        thenTaskStatesSequenceIs(subject.task1State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(subject.task2State, listOf(INITIAL, RUNNING, ERROR))
        thenTaskStatesSequenceIs(subject.task3State, listOf(INITIAL, RUNNING, COMPLETED))
    }

    @Test
    fun runParallelTasksWithError_runsParallelTasksWithError() {
        givenThatStateWillChangeFor(subject.task1State)
        givenThatStateWillChangeFor(subject.task2State)
        givenThatStateWillChangeFor(subject.task3State)
        givenThatParallelTaskWillReturn(mockParallelTask1, TaskExecutionSuccess(10))
        givenThatParallelErrorTaskWillReturn(mockParallelErrorTask, TaskExecutionError(CustomTaskException()))
        givenThatParallelTaskWillReturn(mockParallelTask3, TaskExecutionSuccess(30))
        whenRunParallelTasksWithError()
        thenTaskStatesSequenceIs(subject.task1State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(subject.task2State, listOf(INITIAL, RUNNING, ERROR))
        thenTaskStatesSequenceIs(subject.task3State, listOf(INITIAL, RUNNING, COMPLETED))
    }

    @Test
    fun runMultipleTasks_runsMultipleTasksWithoutError() {
        givenThatStateWillChangeFor(subject.task1State)
        givenThatStateWillChangeFor(subject.task2State)
        givenThatStateWillChangeFor(subject.task3State)
        givenThatMultipleTasksWillReturn(mockMultipleTasks1, TaskExecutionSuccess(10))
        givenThatMultipleTasksWillReturn(mockMultipleTasks2, TaskExecutionSuccess(20))
        givenThatMultipleTasksWillReturn(mockMultipleTasks3, TaskExecutionSuccess(30))
        whenRunMultipleTasks()
        thenTaskStatesSequenceIs(subject.task1State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(subject.task2State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(subject.task3State, listOf(INITIAL, RUNNING, COMPLETED))
    }

    @Test
    fun runCallbackTasksWithError_runsCallbackTasksWithError() {
        givenThatStateWillChangeFor(subject.task1State)
        givenThatStateWillChangeFor(subject.task2State)
        givenThatStateWillChangeFor(subject.task3State)
        givenThatCallbackTaskWillReturn(mockCallbackTask1, TaskExecutionSuccess(10))
        givenThatCallbackTaskWillReturn(mockCallbackTask2, TaskExecutionError(CustomTaskException()))
        givenThatCallbackTaskWillReturn(mockCallbackTask3, TaskExecutionError(CustomTaskException()))
        whenRunCallbackTasksWithError()
        thenTaskStatesSequenceIs(subject.task1State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(subject.task2State, listOf(INITIAL, RUNNING, ERROR))
        thenTaskStatesSequenceIs(subject.task3State, listOf(INITIAL, RUNNING, ERROR))
    }

    @Test
    fun runLongComputationTasks_runsLongComputationTasksWithoutError() {
        givenThatStateWillChangeFor(subject.task1State)
        givenThatStateWillChangeFor(subject.task2State)
        givenThatStateWillChangeFor(subject.task3State)
        givenThatLongComputationTask1WillReturn(TaskExecutionSuccess(10))
        givenThatLongComputationTask2WillReturn(TaskExecutionSuccess(20))
        givenThatLongComputationTask3WillReturn(TaskExecutionSuccess(30))
        whenRunLongComputationTasks()
        thenTaskStatesSequenceIs(subject.task1State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(subject.task2State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(subject.task3State, listOf(INITIAL, RUNNING, COMPLETED))
        thenWaitForCompletionOfLongComputationTasks()
    }

    @Test
    fun cancelLongComputationTask2_cancelsLongComputationTask2BeforeCompletion() {
        givenThatStateWillChangeFor(subject.task1State)
        givenThatStateWillChangeFor(subject.task2State)
        givenThatStateWillChangeFor(subject.task3State)
        givenThatLongComputationTask1WillReturn(TaskExecutionSuccess(10))
        givenThatLongComputationTask2WillBeCancelled()
        givenThatLongComputationTask3WillReturn(TaskExecutionSuccess(30))
        givenThatRunLongComputationTasksHasBeenCalled()
        whenCancelLongComputationTask2()
        thenTaskStatesSequenceIs(subject.task1State, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(subject.task2State, listOf(INITIAL, RUNNING, CANCELLED))
        thenTaskStatesSequenceIs(subject.task3State, listOf(INITIAL, RUNNING, COMPLETED))
        thenWaitForCompletionOfLongComputationTasks()
        thenTaskIsCancelled(mockLongComputationTask2Deferred)
    }

    // endregion Test

    // region Given

    private fun <T> givenThatStateWillChangeFor(liveData: LiveData<T>) {
        trackLiveDataChanges(liveData)
    }

    private fun givenThatSequentialTaskWillReturn(sequentialTask: SequentialTaskUseCase,
                                                  taskExecutionResult: TaskExecutionResult) = runBlocking {
        given(sequentialTask.execute(anyLong(), anyLong(), anyLong())).willReturn(taskExecutionResult)
    }

    private fun givenThatParallelTaskWillReturn(parallelTask: ParallelTaskUseCase,
                                                taskExecutionResult: TaskExecutionResult) = runBlocking {
        given(parallelTask.executeAsync(anyObj<CoroutineScope>(testAppCoroutineScope), anyLong(), anyLong(), anyLong())).willReturn(CompletableDeferred(taskExecutionResult))
    }

    private fun givenThatSequentialErrorTaskWillReturn(sequentialErrorTask: SequentialErrorTaskUseCase,
                                                       taskExecutionResult: TaskExecutionResult) = runBlocking {
        given(sequentialErrorTask.execute(anyLong(), anyLong(), anyLong())).willReturn(taskExecutionResult)
    }

    private fun givenThatParallelErrorTaskWillReturn(parallelErrorTask: ParallelErrorTaskUseCase,
                                                     taskExecutionResult: TaskExecutionResult) = runBlocking {
        given(parallelErrorTask.executeAsync(anyObj<CoroutineScope>(testAppCoroutineScope), anyLong(), anyLong(), anyLong())).willReturn(CompletableDeferred(taskExecutionResult))
    }

    private fun givenThatMultipleTasksWillReturn(multipleTasks: MultipleTasksUseCase,
                                                 taskExecutionResult: TaskExecutionResult) = runBlocking {
        given(multipleTasks.execute(anyLong(), anyLong(), anyLong())).willReturn(taskExecutionResult)
    }

    private fun givenThatCallbackTaskWillReturn(callbackTask: CallbackTaskUseCase,
                                                taskExecutionResult: TaskExecutionResult) = runBlocking {
        given(callbackTask.execute(anyString())).willReturn(taskExecutionResult)
    }

    private fun givenThatLongComputationTaskWillReturn(longComputationTask: LongComputationTaskUseCase,
                                                       taskExecutionDeferred: Deferred<TaskExecutionResult>,
                                                       taskExecutionResult: TaskExecutionResult) = runBlocking {
        given(taskExecutionDeferred.await()).willReturn(taskExecutionResult)
        given(longComputationTask.executeAsync(anyObj<CoroutineScope>(testAppCoroutineScope), anyLong(), anyLong(), anyLong())).willReturn(taskExecutionDeferred)
    }

    private fun givenThatLongComputationTask1WillReturn(taskExecutionResult: TaskExecutionResult) = runBlocking {
        givenThatLongComputationTaskWillReturn(mockLongComputationTask1, mockLongComputationTask1Deferred, taskExecutionResult)
    }

    private fun givenThatLongComputationTask2WillReturn(taskExecutionResult: TaskExecutionResult) = runBlocking {
        givenThatLongComputationTaskWillReturn(mockLongComputationTask2, mockLongComputationTask2Deferred, taskExecutionResult)
    }

    private fun givenThatLongComputationTask3WillReturn(taskExecutionResult: TaskExecutionResult) = runBlocking {
        givenThatLongComputationTaskWillReturn(mockLongComputationTask3, mockLongComputationTask3Deferred, taskExecutionResult)
    }

    private fun givenThatLongComputationTaskWillBeCancelled(longComputationTask: LongComputationTaskUseCase,
                                                            taskExecutionDeferred: Deferred<TaskExecutionResult>) = runBlocking {
        given(longComputationTask.executeAsync(anyObj<CoroutineScope>(testAppCoroutineScope), anyLong(), anyLong(), anyLong())).willReturn(taskExecutionDeferred)
        given(taskExecutionDeferred.await()).willThrow(CancellationException())
    }

    private fun givenThatLongComputationTask2WillBeCancelled() = runBlocking {
        givenThatLongComputationTaskWillBeCancelled(mockLongComputationTask2, mockLongComputationTask2Deferred)
    }

    private fun givenThatRunLongComputationTasksHasBeenCalled() {
        whenRunLongComputationTasks()
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

    private fun whenRunCallbackTasksWithError() {
        subject.runCallbackTasksWithError()
    }

    private fun whenRunLongComputationTasks() {
        subject.runLongComputationTasks()
    }

    private fun whenCancelLongComputationTask2() {
        subject.cancelLongComputationTask2()
    }

    // endregion When

    // region Then

    private fun thenTaskStatesSequenceIs(taskState: LiveData<TaskExecutionState>,
                                         statesSequence: List<TaskExecutionState>) {
        assertThatLiveDataStatesSequenceIs(taskState, statesSequence)
    }

    private fun thenWaitForCompletionOfLongComputationTasks() = runBlocking {
        then(mockLongComputationTask1Deferred).should().await()
        then(mockLongComputationTask2Deferred).should().await()
        then(mockLongComputationTask3Deferred).should().await()
    }

    private fun thenTaskIsCancelled(taskDeferred: Deferred<TaskExecutionResult>) {
        then(taskDeferred).should().cancel()
    }

    // endregion Then
}
