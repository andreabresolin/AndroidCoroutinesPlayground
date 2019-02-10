package andreabresolin.androidcoroutinesplayground.mvvm.viewmodel

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutineScope
import andreabresolin.androidcoroutinesplayground.app.domain.task.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class MVVMViewModelFactory
@Inject constructor(
    private val appCoroutineScope: AppCoroutineScope,
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
    private val channelTask3: ChannelTaskUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MVVMViewModelImpl(
            appCoroutineScope,
            sequentialTask1,
            sequentialTask2,
            sequentialTask3,
            parallelTask1,
            parallelTask2,
            parallelTask3,
            sequentialErrorTask,
            parallelErrorTask,
            multipleTasks1,
            multipleTasks2,
            multipleTasks3,
            callbackTask1,
            callbackTask2,
            callbackTask3,
            longComputationTask1,
            longComputationTask2,
            longComputationTask3,
            channelTask1,
            channelTask2,
            channelTask3
        ) as T
    }
}