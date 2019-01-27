package andreabresolin.androidcoroutinesplayground.app.model

import andreabresolin.androidcoroutinesplayground.app.exception.CustomTaskException

sealed class TaskExecutionResult
data class TaskExecutionSuccess(val result: Long) : TaskExecutionResult()
data class TaskExecutionError(val exception: CustomTaskException) : TaskExecutionResult()