package vn.com.phucars.awesomemovies.di.viewmodel

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import vn.com.phucars.awesomemovies.data.title.TitleData
import vn.com.phucars.awesomemovies.domain.title.mapper.TitleDataRemoteDtoToDomain
import vn.com.phucars.awesomemovies.domain.title.TitleDomain
import vn.com.phucars.awesomemovies.mapper.Mapper

@Module
@InstallIn(ViewModelComponent::class)
object MapperModule {
    @Provides
    fun titleDataRemoteDtoToDomain(): Mapper<TitleData, TitleDomain> = TitleDataRemoteDtoToDomain()
}