package vn.com.phucars.awesomemovies.domain.title

import vn.com.phucars.awesomemovies.data.title.TitleRepository
import vn.com.phucars.awesomemovies.data.title.source.remote.TitleRemoteDataSource
import vn.com.phucars.awesomemovies.domain.ResultDomain
import javax.inject.Inject

class GetTitleById @Inject constructor(private val titleRepository: TitleRepository) {
    suspend operator fun invoke(id: String): ResultDomain<TitleDomain> {
        return titleRepository.getTitleById(id, TitleRemoteDataSource.ParamInfo.CUSTOM_INFO)
    }
}