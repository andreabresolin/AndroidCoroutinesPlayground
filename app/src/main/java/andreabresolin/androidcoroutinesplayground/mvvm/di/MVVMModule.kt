package andreabresolin.androidcoroutinesplayground.mvvm.di

import andreabresolin.androidcoroutinesplayground.app.coroutines.AppCoroutineScope
import andreabresolin.androidcoroutinesplayground.app.coroutines.defaut.LoggingAppCoroutineScope
import andreabresolin.androidcoroutinesplayground.app.di.scope.PerFragment
import andreabresolin.androidcoroutinesplayground.mvvm.viewmodel.MVVMViewModelFactory
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class MVVMModule {

    @PerFragment
    @Binds
    abstract fun bindAppCoroutineScope(appCoroutineScope: LoggingAppCoroutineScope): AppCoroutineScope

    @PerFragment
    @Binds
    abstract fun bindMVVMViewModelFactory(mvvmViewModelFactory: MVVMViewModelFactory): ViewModelProvider.Factory
}