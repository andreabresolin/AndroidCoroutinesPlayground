package andreabresolin.androidcoroutinesplayground.app.model

enum class TaskExecutionState {
    INITIAL,
    RUNNING,
    COMPLETED,
    CANCELLED,
    ERROR
}