package andreabresolin.androidcoroutinesplayground.app.di

import andreabresolin.androidcoroutinesplayground.app.di.scope.PerFragment
import andreabresolin.androidcoroutinesplayground.mvp.di.MVPModule
import andreabresolin.androidcoroutinesplayground.mvp.view.MVPFragment
import andreabresolin.androidcoroutinesplayground.mvvm.di.MVVMModule
import andreabresolin.androidcoroutinesplayground.mvvm.view.MVVMFragment
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector

@Module(includes = [AndroidInjectionModule::class])
abstract class FragmentBindingsModule {

    @PerFragment
    @ContributesAndroidInjector(modules = [MVPModule::class])
    abstract fun bindMVPFragment(): MVPFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [MVVMModule::class])
    abstract fun bindMVVMFragment(): MVVMFragment
}