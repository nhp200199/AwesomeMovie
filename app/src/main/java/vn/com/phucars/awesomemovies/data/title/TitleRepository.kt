package vn.com.phucars.awesomemovies.data.title

import vn.com.phucars.awesomemovies.domain.ResultDomain
import vn.com.phucars.awesomemovies.domain.title.TitleWithRatingDomain

interface TitleRepository {
    suspend fun getGenres(): ResultDomain<List<String?>>
    suspend fun getTitleWithRatingById(id: String): ResultDomain<TitleWithRatingDomain>
    suspend fun getTitleWithRatingListByGenre(genre: String): ResultDomain<List<TitleWithRatingDomain>>
    suspend fun getTitleWithRatingListGroupByGenre(): ResultDomain<Map<String, List<TitleWithRatingDomain>>>
}