package vn.com.phucars.awesomemovies.di.viewmodel

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import vn.com.phucars.awesomemovies.data.authentication.AuthDataSource
import vn.com.phucars.awesomemovies.data.authentication.AuthDataSourceImpl
import vn.com.phucars.awesomemovies.data.authentication.AuthRepository
import vn.com.phucars.awesomemovies.data.authentication.AuthRepositoryImpl
import vn.com.phucars.awesomemovies.data.title.*
import vn.com.phucars.awesomemovies.data.title.source.remote.TitleRemoteDataSourceImpl
import vn.com.phucars.awesomemovies.data.title.source.remote.TitleRemoteDataSource
import vn.com.phucars.awesomemovies.data.user.UserDataSource
import vn.com.phucars.awesomemovies.data.user.UserDataSourceImpl
import vn.com.phucars.awesomemovies.data.user.UserRepository
import vn.com.phucars.awesomemovies.data.user.UserRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {
    @Binds
    abstract fun titleRepository(titleRepositoryImpl: TitleRepositoryImpl): TitleRepository

    @Binds
    abstract fun remoteTitleDataSource(titleRemoteDataSourceImpl: TitleRemoteDataSourceImpl): TitleRemoteDataSource

    @Binds
    abstract fun authRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun authDataSource(authDataSourceImpl: AuthDataSourceImpl): AuthDataSource

    @Binds
    abstract fun userDataSource(userDataSourceImpl: UserDataSourceImpl): UserDataSource

    @Binds
    abstract fun userRepository(userRepository: UserRepositoryImpl): UserRepository
}