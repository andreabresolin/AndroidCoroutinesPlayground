package andreabresolin.androidcoroutinesplayground.mvp.di

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutineScope
import andreabresolin.androidcoroutinesplayground.app.coroutines.defaut.LoggingAppCoroutineScope
import andreabresolin.androidcoroutinesplayground.app.di.scope.PerFragment
import andreabresolin.androidcoroutinesplayground.mvp.presenter.MVPPresenter
import andreabresolin.androidcoroutinesplayground.mvp.presenter.MVPPresenterImpl
import andreabresolin.androidcoroutinesplayground.mvp.view.MVPFragment
import andreabresolin.androidcoroutinesplayground.mvp.view.MVPView
import dagger.Binds
import dagger.Module

@Module
abstract class MVPModule {

    @PerFragment
    @Binds
    abstract fun bindAppCoroutineScope(appCoroutineScope: LoggingAppCoroutineScope): AppCoroutineScope

    @PerFragment
    @Binds
    abstract fun bindMVPPresenter(mvpPresenter: MVPPresenterImpl): MVPPresenter

    @PerFragment
    @Binds
    abstract fun bindMVPView(mvpFragment: MVPFragment): MVPView
}