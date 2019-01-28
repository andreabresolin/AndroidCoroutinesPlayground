package andreabresolin.androidcoroutinesplayground.mvvm.view

import andreabresolin.androidcoroutinesplayground.R
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionState
import andreabresolin.androidcoroutinesplayground.app.view.BaseFragment
import andreabresolin.androidcoroutinesplayground.mvvm.viewmodel.MVVMViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.airbnb.paris.extensions.style
import kotlinx.android.synthetic.main.fragment_tasks_common.*
import javax.inject.Inject

class MVVMFragment : BaseFragment() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MVVMViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_mvvm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MVVMViewModel::class.java)
        observeViewState()
        setUpListeners()
    }

    private fun observeViewState() {
        viewModel.task1State.observe(this, task1StateObserver)
        viewModel.task2State.observe(this, task2StateObserver)
        viewModel.task3State.observe(this, task3StateObserver)
    }

    private fun setUpListeners() {
        runSequentialBtn.setOnClickListener { onRunSequentialBtnClicked() }
        runParallelBtn.setOnClickListener { onRunParallelBtnClicked() }
        runSequentialWithErrorBtn.setOnClickListener { onRunSequentialWithErrorBtnClicked() }
        runParallelWithErrorBtn.setOnClickListener { onRunParallelWithErrorBtnClicked() }
        runMultipleBtn.setOnClickListener { onRunMultipleBtnClicked() }
        runCallbackWithErrorBtn.setOnClickListener { onRunCallbackWithErrorBtnClicked() }
    }

    private val task1StateObserver = Observer<TaskExecutionState> { newState -> applyTaskStyleForState(task1Box, newState) }
    private val task2StateObserver = Observer<TaskExecutionState> { newState -> applyTaskStyleForState(task2Box, newState) }
    private val task3StateObserver = Observer<TaskExecutionState> { newState -> applyTaskStyleForState(task3Box, newState) }

    private fun applyTaskStyleForState(taskView: View, taskExecutionState: TaskExecutionState) {
        when (taskExecutionState) {
            TaskExecutionState.INITIAL -> taskView.style(R.style.TaskBoxInitialState)
            TaskExecutionState.RUNNING -> taskView.style(R.style.TaskBoxRunningState)
            TaskExecutionState.COMPLETED -> taskView.style(R.style.TaskBoxCompletedState)
            TaskExecutionState.ERROR -> taskView.style(R.style.TaskBoxErrorState)
        }
    }

    private fun onRunSequentialBtnClicked() {
        viewModel.runSequentialTasks()
    }

    private fun onRunParallelBtnClicked() {
        viewModel.runParallelTasks()
    }

    private fun onRunSequentialWithErrorBtnClicked() {
        viewModel.runSequentialTasksWithError()
    }

    private fun onRunParallelWithErrorBtnClicked() {
        viewModel.runParallelTasksWithError()
    }

    private fun onRunMultipleBtnClicked() {
        viewModel.runMultipleTasks()
    }

    private fun onRunCallbackWithErrorBtnClicked() {
        viewModel.runCallbackTasksWithError()
    }
}
