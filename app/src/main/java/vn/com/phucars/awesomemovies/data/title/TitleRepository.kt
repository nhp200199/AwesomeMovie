package vn.com.phucars.awesomemovies.data.title

import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.domain.title.TitleWithRatingDomain

interface TitleRepository {
    suspend fun getGenres(): ResultData<List<String?>>
    suspend fun getTitleWithRatingById(id: String): ResultData<TitleWithRatingDomain>
    suspend fun getTitlesWithRatingByGenre(genre: String): ResultData<List<TitleWithRatingDomain>>
}