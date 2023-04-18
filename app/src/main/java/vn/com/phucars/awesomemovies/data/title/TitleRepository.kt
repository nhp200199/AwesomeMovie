package vn.com.phucars.awesomemovies.data.title

import vn.com.phucars.awesomemovies.data.ResultData

interface TitleRepository {
    suspend fun getTitlesByGenre(genre: String): ResultData<List<TitleData>>
    suspend fun getTitleById(id: String): ResultData<TitleData>
    suspend fun getTitleRating(titleId: String): ResultData<TitleData.Rating>
    suspend fun getGenres(): ResultData<List<String?>>
    suspend fun getTitlesWithRatingByGenre(genre: String): ResultData<List<TitleWithRatingData>>
}