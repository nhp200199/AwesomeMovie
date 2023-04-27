package vn.com.phucars.awesomemovies.data.title

import vn.com.phucars.awesomemovies.data.ResultData

interface TitleRepository {
    suspend fun getGenres(): ResultData<List<String?>>
    suspend fun getTitleWithRatingById(id: String): ResultData<TitleWithRatingData>
    suspend fun getTitlesWithRatingByGenre(genre: String): ResultData<List<TitleWithRatingData>>
}