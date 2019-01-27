package andreabresolin.androidcoroutinesplayground.mvp.view

import andreabresolin.androidcoroutinesplayground.app.model.TaskExecutionState

interface MVPView {

    fun updateTaskExecutionState(taskNumber: Int, taskExecutionState: TaskExecutionState)
}