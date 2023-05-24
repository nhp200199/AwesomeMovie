package vn.com.phucars.awesomemovies.domain.title

import vn.com.phucars.awesomemovies.data.title.TitleRepository
import vn.com.phucars.awesomemovies.domain.ResultDomain
import javax.inject.Inject

class GetTitleWithRatingListGroupByGenre @Inject constructor(private val titleRepository: TitleRepository) {
    suspend fun getTitleWithRatingListGroupByGenre(): ResultDomain<Map<String, List<TitleWithRatingDomain>>> {
        return titleRepository.getTitleWithRatingListGroupByGenre()
    }
}