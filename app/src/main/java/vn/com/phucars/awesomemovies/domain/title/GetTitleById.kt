package vn.com.phucars.awesomemovies.domain.title

import vn.com.phucars.awesomemovies.data.title.TitleRepository
import vn.com.phucars.awesomemovies.domain.ResultDomain
import javax.inject.Inject

class GetTitleById @Inject constructor(private val titleRepository: TitleRepository) {
    suspend fun invoke(id: String, info: String? = null): ResultDomain<TitleWithRatingDomain> {
        return titleRepository.getTitleDetailById(id, info)
    }
}