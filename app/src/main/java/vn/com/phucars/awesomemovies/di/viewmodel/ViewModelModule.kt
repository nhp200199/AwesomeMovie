package vn.com.phucars.awesomemovies.di.viewmodel

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import vn.com.phucars.awesomemovies.data.title.*
import vn.com.phucars.awesomemovies.data.title.source.remote.TitleRemoteDataSourceImpl
import vn.com.phucars.awesomemovies.data.title.source.remote.TitleRemoteDataSource

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {
    @Binds
    abstract fun titleRepository(titleRepositoryImpl: TitleRepositoryImpl): TitleRepository

    @Binds
    abstract fun remoteTitleDataSource(titleRemoteDataSourceImpl: TitleRemoteDataSourceImpl): TitleRemoteDataSource
}