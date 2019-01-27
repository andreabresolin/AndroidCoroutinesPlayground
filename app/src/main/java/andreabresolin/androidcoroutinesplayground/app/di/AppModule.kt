package andreabresolin.androidcoroutinesplayground.app.di

import andreabresolin.androidcoroutinesplayground.app.coroutines.CoroutineDispatcherProvider
import andreabresolin.androidcoroutinesplayground.app.coroutines.defaut.DefaultCoroutineDispatcherProvider
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Singleton
    @Binds
    abstract fun bindCoroutineDispatcherProvider(coroutineDispatcherProvider: DefaultCoroutineDispatcherProvider): CoroutineDispatcherProvider
}