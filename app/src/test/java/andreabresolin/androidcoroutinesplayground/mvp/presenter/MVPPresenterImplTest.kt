package andreabresolin.androidcoroutinesplayground.mvp.presenter

import andreabresolin.androidcoroutinesplayground.app.domain.task.*
import andreabresolin.androidcoroutinesplayground.app.exception.CustomTaskException
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionError
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionResult
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionState
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionState.*
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionSuccess
import andreabresolin.androidcoroutinesplayground.mvp.view.MVPView
import andreabresolin.androidcoroutinesplayground.testing.BasePresenterTest
import andreabresolin.androidcoroutinesplayground.testing.KotlinTestUtils.Companion.anyObj
import andreabresolin.androidcoroutinesplayground.testing.KotlinTestUtils.Companion.captureObj
import andreabresolin.androidcoroutinesplayground.testing.MockableDeferred
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.Mockito.*
import java.io.IOException

class MVPPresenterImplTest : BasePresenterTest() {

    @Mock
    private lateinit var mockView: MVPView
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
    private lateinit var mockLongComputationTask1Deferred: MockableDeferred<TaskExecutionResult>
    @Mock
    private lateinit var mockLongComputationTask2Deferred: MockableDeferred<TaskExecutionResult>
    @Mock
    private lateinit var mockLongComputationTask3Deferred: MockableDeferred<TaskExecutionResult>
    @Mock
    private lateinit var mockChannelTask1: ChannelTaskUseCase
    @Mock
    private lateinit var mockChannelTask2: ChannelTaskUseCase
    @Mock
    private lateinit var mockChannelTask3: ChannelTaskUseCase
    @Mock
    private lateinit var mockChannelTask1Deferred: MockableDeferred<TaskExecutionResult>
    @Mock
    private lateinit var mockChannelTask2Deferred: MockableDeferred<TaskExecutionResult>
    @Mock
    private lateinit var mockChannelTask3Deferred: MockableDeferred<TaskExecutionResult>
    @Mock
    private lateinit var mockExceptionsTask: ExceptionsTaskUseCase
    @Mock
    private lateinit var mockExceptionsTask2Deferred: MockableDeferred<TaskExecutionResult>
    @Mock
    private lateinit var mockExceptionsTask3Deferred: MockableDeferred<TaskExecutionResult>

    private lateinit var subject: MVPPresenterImpl

    private var givenChannel1Items = listOf<Long>()
    private var givenChannel2Items = listOf<Long>()
    private var givenChannel3Items = listOf<Long>()
    private var givenBackpressureChannel3Items = listOf<Long>()

    @Before
    fun before() {
        subject = MVPPresenterImpl(
            testAppCoroutineScope,
            mockView,
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
            mockLongComputationTask3,
            mockChannelTask1,
            mockChannelTask2,
            mockChannelTask3,
            mockExceptionsTask)
    }

    // region Test

    @Test
    fun runSequentialTasks_runsSequentialTasksWithoutError() {
        givenThatSequentialTaskWillReturn(mockSequentialTask1, TaskExecutionSuccess(10))
        givenThatSequentialTaskWillReturn(mockSequentialTask2, TaskExecutionSuccess(20))
        givenThatSequentialTaskWillReturn(mockSequentialTask3, TaskExecutionSuccess(30))
        whenRunSequentialTasks()
        thenTaskStatesSequenceIs(1, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(2, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(3, listOf(INITIAL, RUNNING, COMPLETED))
        thenNoMoreInteractionsWithView()
    }

    @Test
    fun runParallelTasks_runsParallelTasksWithoutError() {
        givenThatParallelTaskWillReturn(mockParallelTask1, TaskExecutionSuccess(10))
        givenThatParallelTaskWillReturn(mockParallelTask2, TaskExecutionSuccess(20))
        givenThatParallelTaskWillReturn(mockParallelTask3, TaskExecutionSuccess(30))
        whenRunParallelTasks()
        thenTaskStatesSequenceIs(1, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(2, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(3, listOf(INITIAL, RUNNING, COMPLETED))
        thenNoMoreInteractionsWithView()
    }

    @Test
    fun runSequentialTasksWithError_runsSequentialTasksWithError() {
        givenThatSequentialTaskWillReturn(mockSequentialTask1, TaskExecutionSuccess(10))
        givenThatSequentialErrorTaskWillReturn(mockSequentialErrorTask, TaskExecutionError(CustomTaskException()))
        givenThatSequentialTaskWillReturn(mockSequentialTask3, TaskExecutionSuccess(30))
        whenRunSequentialTasksWithError()
        thenTaskStatesSequenceIs(1, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(2, listOf(INITIAL, RUNNING, ERROR))
        thenTaskStatesSequenceIs(3, listOf(INITIAL, RUNNING, COMPLETED))
        thenNoMoreInteractionsWithView()
    }

    @Test
    fun runParallelTasksWithError_runsParallelTasksWithError() {
        givenThatParallelTaskWillReturn(mockParallelTask1, TaskExecutionSuccess(10))
        givenThatParallelErrorTaskWillReturn(mockParallelErrorTask, TaskExecutionError(CustomTaskException()))
        givenThatParallelTaskWillReturn(mockParallelTask3, TaskExecutionSuccess(30))
        whenRunParallelTasksWithError()
        thenTaskStatesSequenceIs(1, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(2, listOf(INITIAL, RUNNING, ERROR))
        thenTaskStatesSequenceIs(3, listOf(INITIAL, RUNNING, COMPLETED))
        thenNoMoreInteractionsWithView()
    }

    @Test
    fun runMultipleTasks_runsMultipleTasksWithoutError() {
        givenThatMultipleTasksWillReturn(mockMultipleTasks1, TaskExecutionSuccess(10))
        givenThatMultipleTasksWillReturn(mockMultipleTasks2, TaskExecutionSuccess(20))
        givenThatMultipleTasksWillReturn(mockMultipleTasks3, TaskExecutionSuccess(30))
        whenRunMultipleTasks()
        thenTaskStatesSequenceIs(1, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(2, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(3, listOf(INITIAL, RUNNING, COMPLETED))
        thenNoMoreInteractionsWithView()
    }

    @Test
    fun runCallbackTasksWithError_runsCallbackTasksWithError() {
        givenThatCallbackTaskWillThrow(mockCallbackTask1, CustomTaskException())
        givenThatCallbackTaskWillReturn(mockCallbackTask2, TaskExecutionSuccess(10))
        givenThatCallbackTaskWillBeCancelled(mockCallbackTask3)
        whenRunCallbackTasksWithError()
        thenTaskStatesSequenceIs(1, listOf(INITIAL, RUNNING, ERROR))
        thenTaskStatesSequenceIs(2, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(3, listOf(INITIAL, RUNNING, CANCELLED))
        thenNoMoreInteractionsWithView()
    }

    @Test
    fun runLongComputationTasks_runsLongComputationTasksWithoutError() {
        givenThatLongComputationTask1WillReturn(TaskExecutionSuccess(10))
        givenThatLongComputationTask2WillReturn(TaskExecutionSuccess(20))
        givenThatLongComputationTask3WillReturn(TaskExecutionSuccess(30))
        whenRunLongComputationTasks()
        thenTaskStatesSequenceIs(1, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(2, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(3, listOf(INITIAL, RUNNING, COMPLETED))
        thenWaitForCompletionOfLongComputationTasks()
        thenNoMoreInteractionsWithView()
    }

    @Test
    fun cancelLongComputationTask2_cancelsLongComputationTask2BeforeCompletion() {
        givenThatLongComputationTask1WillReturn(TaskExecutionSuccess(10))
        givenThatLongComputationTask2WillBeCancelled()
        givenThatLongComputationTask3WillReturn(TaskExecutionSuccess(30))
        givenThatRunLongComputationTasksHasBeenCalled()
        whenCancelLongComputationTask2()
        thenTaskStatesSequenceIs(1, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(2, listOf(INITIAL, RUNNING, CANCELLED))
        thenTaskStatesSequenceIs(3, listOf(INITIAL, RUNNING, COMPLETED))
        thenWaitForCompletionOfLongComputationTasks()
        thenTaskIsCancelled(mockLongComputationTask2Deferred)
        thenNoMoreInteractionsWithView()
    }

    @Test
    fun runLongComputationTasksWithTimeout_completesLongComputationTasksIfFasterThanTimeout() {
        givenThatLongComputationTask1WillReturn(TaskExecutionSuccess(10))
        givenThatLongComputationTask2WillReturn(TaskExecutionSuccess(20))
        givenThatLongComputationTask3WillReturn(TaskExecutionSuccess(30))
        whenRunLongComputationTasksWithTimeout()
        thenTaskStatesSequenceIs(1, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(2, listOf(INITIAL, RUNNING, COMPLETED))
        thenTaskStatesSequenceIs(3, listOf(INITIAL, RUNNING, COMPLETED))
        thenWaitForCompletionOfLongComputationTasks()
        thenNoMoreInteractionsWithView()
    }

    @Test
    fun runLongComputationTasksWithTimeout_cancelsLongComputationTasksIfSlowerThanTimeout() {
        givenThatLongComputationTask1WillTimeout()
        givenThatLongComputationTask2WillTimeout()
        givenThatLongComputationTask3WillTimeout()
        whenRunLongComputationTasksWithTimeout()
        thenTaskStatesSequenceIs(1, listOf(INITIAL, RUNNING, CANCELLED))
        thenTaskStatesSequenceIs(2, listOf(INITIAL, RUNNING, CANCELLED))
        thenTaskStatesSequenceIs(3, listOf(INITIAL, RUNNING, CANCELLED))
        thenWaitForCompletionOfLongComputationTasks()
        thenNoMoreInteractionsWithView()
    }

    @Test
    fun runChannelsTasks_handlesAllItemsSentByChannelsTasks() {
        givenThatChannelTask1WillReturn(TaskExecutionSuccess(1))
        givenThatChannelTask2WillReturn(TaskExecutionSuccess(2))
        givenThatChannelTask3WillReturn(TaskExecutionSuccess(3))
        givenThatChannel1WillSend(listOf(10L, 20L, 30L, 40L, 50L))
        givenThatChannel2WillSend(listOf(100L, 200L, 300L))
        givenThatChannel3WillSend(listOf(1000L, 2000L, 3000L, 4000L))
        givenThatBackpressureChannel3WillSend(listOf(10000L, 20000L))
        whenRunChannelsTasks()
        thenTaskStatesSequenceIs(1,
            listOf(
                INITIAL,
                RUNNING, INITIAL,
                RUNNING, INITIAL,
                RUNNING, INITIAL,
                RUNNING, INITIAL,
                RUNNING, INITIAL,
                COMPLETED))
        thenTaskStatesSequenceIs(2,
            listOf(
                INITIAL,
                RUNNING, INITIAL,
                RUNNING, INITIAL,
                RUNNING, INITIAL,
                COMPLETED))
        thenTaskStatesSequenceIs(3,
            listOf(
                INITIAL,
                RUNNING, INITIAL,
                RUNNING, INITIAL,
                RUNNING, INITIAL,
                RUNNING, INITIAL,
                ERROR,
                ERROR,
                COMPLETED))
        thenWaitForCompletionOfChannelsTasks()
        thenNoMoreInteractionsWithView()
    }

    @Test
    fun runExceptionsTasks_runsExceptionsTasksWithError() {
        givenThatExceptionsTaskExecuteWillThrow(CustomTaskException::class.java)
        givenThatExceptionsTaskExecuteAsyncWillThrow(CustomTaskException::class.java)
        givenThatExceptionsTaskExecuteWithRepositoryAsyncWillThrow(IOException::class.java)
        whenRunExceptionsTasks()
        thenTaskStatesSequenceIs(1, listOf(INITIAL, RUNNING, ERROR))
        thenTaskStatesSequenceIs(2, listOf(INITIAL, RUNNING, ERROR))
        thenTaskStatesSequenceIs(3, listOf(INITIAL, RUNNING, ERROR))
    }

    // endregion Test

    // region Given

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

    private fun givenThatCallbackTaskWillThrow(callbackTask: CallbackTaskUseCase,
                                               exception: Exception) = runBlocking {
        given(callbackTask.execute(anyString())).willAnswer { throw exception }
    }

    private fun givenThatCallbackTaskWillBeCancelled(callbackTask: CallbackTaskUseCase) = runBlocking {
        given(callbackTask.execute(anyString())).willThrow(CancellationException())
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

    private fun givenThatLongComputationTaskWillTimeout(longComputationTask: LongComputationTaskUseCase,
                                                        taskExecutionDeferred: Deferred<TaskExecutionResult>) = runBlocking {
        given(longComputationTask.executeAsync(anyObj<CoroutineScope>(testAppCoroutineScope), anyLong(), anyLong(), anyLong())).willReturn(taskExecutionDeferred)
        given(taskExecutionDeferred.await()).willThrow(mock(TimeoutCancellationException::class.java))
    }

    private fun givenThatLongComputationTask1WillTimeout() {
        givenThatLongComputationTaskWillTimeout(mockLongComputationTask1, mockLongComputationTask1Deferred)
    }

    private fun givenThatLongComputationTask2WillTimeout() {
        givenThatLongComputationTaskWillTimeout(mockLongComputationTask2, mockLongComputationTask2Deferred)
    }

    private fun givenThatLongComputationTask3WillTimeout() {
        givenThatLongComputationTaskWillTimeout(mockLongComputationTask3, mockLongComputationTask3Deferred)
    }

    private fun givenThatChannelTaskWillReturn(channelTask: ChannelTaskUseCase,
                                               taskExecutionDeferred: Deferred<TaskExecutionResult>,
                                               taskExecutionResult: TaskExecutionResult,
                                               hasBackpressureChannel: Boolean) = runBlocking {
        given(taskExecutionDeferred.await()).willReturn(taskExecutionResult)
        given(channelTask.executeAsync(
            anyObj<CoroutineScope>(testAppCoroutineScope),
            anyLong(),
            anyLong(),
            anyObj(Channel()),
            if (hasBackpressureChannel) anyObj<Channel<Long>>(Channel()) else eq(null))).willReturn(taskExecutionDeferred)
    }

    private fun givenThatChannelTask1WillReturn(taskExecutionResult: TaskExecutionResult) = runBlocking {
        givenThatChannelTaskWillReturn(mockChannelTask1, mockChannelTask1Deferred, taskExecutionResult, false)
    }

    private fun givenThatChannelTask2WillReturn(taskExecutionResult: TaskExecutionResult) = runBlocking {
        givenThatChannelTaskWillReturn(mockChannelTask2, mockChannelTask2Deferred, taskExecutionResult, false)
    }

    private fun givenThatChannelTask3WillReturn(taskExecutionResult: TaskExecutionResult) = runBlocking {
        givenThatChannelTaskWillReturn(mockChannelTask3, mockChannelTask3Deferred, taskExecutionResult, true)
    }

    private fun givenThatChannel1WillSend(items: List<Long>) {
        givenChannel1Items = items
    }

    private fun givenThatChannel2WillSend(items: List<Long>) {
        givenChannel2Items = items
    }

    private fun givenThatChannel3WillSend(items: List<Long>) {
        givenChannel3Items = items
    }

    private fun givenThatBackpressureChannel3WillSend(items: List<Long>) {
        givenBackpressureChannel3Items = items
    }

    private fun <T: Exception> givenThatExceptionsTaskExecuteWillThrow(exception: Class<T>) = runBlocking {
        given(mockExceptionsTask.execute(anyLong(), anyLong(), anyLong())).willAnswer { throw exception.newInstance() }
    }

    private fun <T: Exception> givenThatExceptionsTaskExecuteAsyncWillThrow(exception: Class<T>) = runBlocking {
        given(mockExceptionsTask2Deferred.await()).willAnswer { throw exception.newInstance() }
        given(mockExceptionsTask.executeAsync(anyObj<CoroutineScope>(testAppCoroutineScope), anyLong(), anyLong(), anyLong())).willReturn(mockExceptionsTask2Deferred)
    }

    private fun <T: Exception> givenThatExceptionsTaskExecuteWithRepositoryAsyncWillThrow(exception: Class<T>) = runBlocking {
        given(mockExceptionsTask3Deferred.await()).willAnswer { throw exception.newInstance() }
        given(mockExceptionsTask.executeWithRepositoryAsync(anyObj<CoroutineScope>(testAppCoroutineScope), anyLong(), anyLong(), anyLong())).willReturn(mockExceptionsTask3Deferred)
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

    private fun whenRunLongComputationTasksWithTimeout() {
        subject.runLongComputationTasksWithTimeout()
    }

    private fun whenRunChannelsTasks() = runBlocking {
        subject.runChannelsTasks()

        val mockSendChannel = mock(Channel::class.java) as SendChannel<Long>
        val channel1Captor = ArgumentCaptor.forClass(SendChannel::class.java)
        val channel2Captor = ArgumentCaptor.forClass(SendChannel::class.java)
        val channel3Captor = ArgumentCaptor.forClass(SendChannel::class.java)
        val backpressureChannel3Captor = ArgumentCaptor.forClass(SendChannel::class.java)

        then(mockChannelTask1).should().executeAsync(
            anyObj<CoroutineScope>(testAppCoroutineScope),
            anyLong(),
            anyLong(),
            captureObj(channel1Captor, mockSendChannel) as SendChannel<Long>,
            eq(null))
        then(mockChannelTask2).should().executeAsync(
            anyObj<CoroutineScope>(testAppCoroutineScope),
            anyLong(),
            anyLong(),
            captureObj(channel2Captor, mockSendChannel) as SendChannel<Long>,
            eq(null))
        then(mockChannelTask3).should().executeAsync(
            anyObj<CoroutineScope>(testAppCoroutineScope),
            anyLong(),
            anyLong(),
            captureObj(channel3Captor, mockSendChannel) as SendChannel<Long>,
            captureObj(backpressureChannel3Captor, mockSendChannel) as SendChannel<Long>)

        val givenChannel1 = channel1Captor.value as Channel<Long>
        val givenChannel2 = channel2Captor.value as Channel<Long>
        val givenChannel3 = channel3Captor.value as Channel<Long>
        val givenBackpressureChannel3 = backpressureChannel3Captor.value as Channel<Long>

        givenChannel1Items.forEach { givenChannel1.send(it) }
        givenChannel2Items.forEach { givenChannel2.send(it) }
        givenChannel3Items.forEach { givenChannel3.send(it) }
        givenBackpressureChannel3Items.forEach { givenBackpressureChannel3.send(it) }

        givenChannel1.close()
        givenChannel2.close()
        givenChannel3.close()
        givenBackpressureChannel3.close()
    }

    private fun whenRunExceptionsTasks() {
        subject.runExceptionsTasks()
    }

    // endregion When

    // region Then

    private fun thenTaskStatesSequenceIs(taskNumber: Int, states: List<TaskExecutionState>) {
        val inOrder = inOrder(mockView)
        var lastState: TaskExecutionState? = null
        var times = 0

        for (state in states) {
            if (lastState != null && state != lastState) {
                then(mockView).should(inOrder, times(times)).updateTaskExecutionState(taskNumber, lastState)
                times = 0
            }

            lastState = state
            times++
        }

        if (lastState != null) {
            then(mockView).should(inOrder, times(times)).updateTaskExecutionState(taskNumber, lastState)
        }
    }

    private fun thenNoMoreInteractionsWithView() {
        then(mockView).shouldHaveNoMoreInteractions()
    }

    private fun thenWaitForCompletionOfLongComputationTasks() = runBlocking {
        then(mockLongComputationTask1Deferred).should().await()
        then(mockLongComputationTask2Deferred).should().await()
        then(mockLongComputationTask3Deferred).should().await()
    }

    private fun thenTaskIsCancelled(taskDeferred: Deferred<TaskExecutionResult>) {
        then(taskDeferred).should().cancel()
    }

    private fun thenWaitForCompletionOfChannelsTasks() = runBlocking {
        then(mockChannelTask1Deferred).should().await()
        then(mockChannelTask2Deferred).should().await()
        then(mockChannelTask3Deferred).should().await()
    }

    // endregion Then
}