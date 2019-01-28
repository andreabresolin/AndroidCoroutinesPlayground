package andreabresolin.androidcoroutinesplayground.mvp.presenter

interface MVPPresenter {

    fun runSequentialTasks()

    fun runParallelTasks()

    fun runSequentialTasksWithError()

    fun runParallelTasksWithError()

    fun runMultipleTasks()

    fun runCallbackTasksWithError()

    fun cancelAllTasks()
}