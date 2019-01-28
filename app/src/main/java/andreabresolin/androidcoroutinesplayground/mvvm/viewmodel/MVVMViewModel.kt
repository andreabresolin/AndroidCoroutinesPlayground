package andreabresolin.androidcoroutinesplayground.mvvm.viewmodel

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutineScope
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionState
import andreabresolin.androidcoroutinesplayground.app.presentation.BaseViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class MVVMViewModel
constructor(appCoroutineScope: AppCoroutineScope) : BaseViewModel(appCoroutineScope) {

    abstract val task1State: LiveData<TaskExecutionState>
    abstract val task2State: LiveData<TaskExecutionState>
    abstract val task3State: LiveData<TaskExecutionState>

    abstract fun runSequentialTasks()

    abstract fun runParallelTasks()

    abstract fun runSequentialTasksWithError()

    abstract fun runParallelTasksWithError()

    abstract fun runMultipleTasks()

    abstract fun runCallbackTasksWithError()
}