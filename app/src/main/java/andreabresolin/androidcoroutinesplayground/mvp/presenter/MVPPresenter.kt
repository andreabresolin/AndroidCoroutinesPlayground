package andreabresolin.androidcoroutinesplayground.mvp.presenter

interface MVPPresenter {

    fun runSequentialTasks()

    fun runParallelTasks()

    fun runSequentialTasksWithError()

    fun runParallelTasksWithError()

    fun runMultipleTasks()

    fun runCallbackTasks()

    fun cancelAllTasks()
}