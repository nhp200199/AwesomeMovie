package vn.com.phucars.awesomemovies.di.viewmodel

import com.google.firebase.auth.FirebaseUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import vn.com.phucars.awesomemovies.data.authentication.AuthUser
import vn.com.phucars.awesomemovies.data.title.DetailTitleRemoteData
import vn.com.phucars.awesomemovies.data.title.TitleWithRatingRemoteData
import vn.com.phucars.awesomemovies.domain.authentication.AuthUserMapper
import vn.com.phucars.awesomemovies.domain.title.NewTitleWithRatingRemoteDtoToDomain
import vn.com.phucars.awesomemovies.domain.title.TitleWithRatingDomain
import vn.com.phucars.awesomemovies.domain.title.TitleWithRatingRemoteDtoToDomain
import vn.com.phucars.awesomemovies.mapper.Mapper

@Module
@InstallIn(ViewModelComponent::class)
object MapperModule {
    @Provides
    fun titleWithRatingRemoteDtoToDomain(): Mapper<TitleWithRatingRemoteData, TitleWithRatingDomain> = TitleWithRatingRemoteDtoToDomain()

    @Provides
    fun newTitleWithRatingRemoteDtoToDomain(): Mapper<DetailTitleRemoteData, TitleWithRatingDomain> = NewTitleWithRatingRemoteDtoToDomain()

    @Provides
    fun firebaseAuthUserToDomain(): Mapper<FirebaseUser, String> = AuthUserMapper()
}