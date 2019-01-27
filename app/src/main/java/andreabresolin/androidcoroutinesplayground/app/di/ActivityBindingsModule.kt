package andreabresolin.androidcoroutinesplayground.app.di

import andreabresolin.androidcoroutinesplayground.MainActivity
import andreabresolin.androidcoroutinesplayground.app.di.scope.PerActivity
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector

@Module(includes = [AndroidInjectionModule::class])
abstract class ActivityBindingsModule {

    @PerActivity
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity
}