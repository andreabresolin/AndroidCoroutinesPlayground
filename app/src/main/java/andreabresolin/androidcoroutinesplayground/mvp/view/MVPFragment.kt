package andreabresolin.androidcoroutinesplayground.mvp.view

import andreabresolin.androidcoroutinesplayground.R
import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionState
import andreabresolin.androidcoroutinesplayground.app.view.BaseFragment
import andreabresolin.androidcoroutinesplayground.mvp.presenter.MVPPresenter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.paris.extensions.style
import kotlinx.android.synthetic.main.fragment_tasks_common.*
import javax.inject.Inject

class MVPFragment : BaseFragment(), MVPView {

    @Inject
    internal lateinit var presenter: MVPPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_mvp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpListeners()
    }

    override fun onStop() {
        presenter.cancelAllTasks()
        super.onStop()
    }

    private fun setUpListeners() {
        runSequentialBtn.setOnClickListener { onRunSequentialBtnClicked() }
        runParallelBtn.setOnClickListener { onRunParallelBtnClicked() }
        runSequentialWithErrorBtn.setOnClickListener { onRunSequentialWithErrorBtnClicked() }
        runParallelWithErrorBtn.setOnClickListener { onRunParallelWithErrorBtnClicked() }
        runMultipleBtn.setOnClickListener { onRunMultipleBtnClicked() }
        runCallbackBtn.setOnClickListener { onRunCallbackBtnClicked() }
    }

    private fun onRunSequentialBtnClicked() {
        presenter.runSequentialTasks()
    }

    private fun onRunParallelBtnClicked() {
        presenter.runParallelTasks()
    }

    private fun onRunSequentialWithErrorBtnClicked() {
        presenter.runSequentialTasksWithError()
    }

    private fun onRunParallelWithErrorBtnClicked() {
        presenter.runParallelTasksWithError()
    }

    private fun onRunMultipleBtnClicked() {
        presenter.runMultipleTasks()
    }

    private fun onRunCallbackBtnClicked() {
        presenter.runCallbackTasks()
    }

    private fun applyTaskStyleForState(taskView: View, taskExecutionState: TaskExecutionState) {
        when (taskExecutionState) {
            TaskExecutionState.INITIAL -> taskView.style(R.style.TaskBoxInitialState)
            TaskExecutionState.RUNNING -> taskView.style(R.style.TaskBoxRunningState)
            TaskExecutionState.COMPLETED -> taskView.style(R.style.TaskBoxCompletedState)
            TaskExecutionState.ERROR -> taskView.style(R.style.TaskBoxErrorState)
        }
    }

    override fun updateTaskExecutionState(taskNumber: Int, taskExecutionState: TaskExecutionState) {
        val taskView: View? = when (taskNumber) {
            1 -> task1Box
            2 -> task2Box
            3 -> task3Box
            else -> null
        }

        taskView?.let { applyTaskStyleForState(it, taskExecutionState) }
    }
}