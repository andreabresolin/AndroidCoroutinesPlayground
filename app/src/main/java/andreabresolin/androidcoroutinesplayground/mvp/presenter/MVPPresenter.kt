package andreabresolin.androidcoroutinesplayground.mvp.presenter

interface MVPPresenter {

    fun runSequentialTasks()

    fun runParallelTasks()

    fun runSequentialTasksWithError()

    fun runParallelTasksWithError()

    fun runMultipleTasks()

    fun runCallbackTasksWithError()

    fun runLongComputationTasks()

    fun cancelLongComputationTask1()

    fun cancelLongComputationTask2()

    fun cancelLongComputationTask3()

    fun runLongComputationTasksWithTimeout()

    fun runChannelsTasks()

    fun runExceptionsTasks()

    fun cancelJobs()
}