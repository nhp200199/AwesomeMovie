package vn.com.phucars.awesomemovies.di.app

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.com.phucars.awesomemovies.dispatcher.DefaultDispatcherProvider
import vn.com.phucars.awesomemovies.dispatcher.DispatcherProvider

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatcherProviderModule {
    @Binds
    abstract fun defaultDispatcherProvider(defaultDispatcherProvider: DefaultDispatcherProvider): DispatcherProvider

    companion object {
        @Provides
        fun defaultProvider(): DefaultDispatcherProvider = DefaultDispatcherProvider()
    }
}