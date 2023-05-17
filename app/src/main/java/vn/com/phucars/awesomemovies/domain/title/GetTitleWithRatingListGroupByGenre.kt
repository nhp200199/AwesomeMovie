package vn.com.phucars.awesomemovies.domain.title

import vn.com.phucars.awesomemovies.data.title.TitleRepository
import vn.com.phucars.awesomemovies.domain.ResultDomain

class GetTitleWithRatingListGroupByGenre(private val titleRepository: TitleRepository) {
    suspend fun getTitleWithRatingListGroupByGenre(): ResultDomain<Map<String, List<TitleWithRatingDomain>>> {
        return titleRepository.getTitleWithRatingListGroupByGenre()
    }
}