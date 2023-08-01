package vn.com.phucars.awesomemovies.data.title.source.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.title.TitleData
import vn.com.phucars.awesomemovies.data.title.TitleWithRatingRemoteData
import javax.inject.Inject

class TitleByGenrePagingDataSource @Inject constructor(
    private val titleRemoteDataSource: TitleRemoteDataSource,
    private val genre: String
) : BaseRemotePagingDataSource<TitleWithRatingRemoteData>() {

    override suspend fun requestData(page: Int): ResultData<List<TitleWithRatingRemoteData>> {
        return withContext(Dispatchers.IO) {
            val titlesByGenre = titleRemoteDataSource.getTitleListByGenre(genre, page = page)
            when (titlesByGenre) {
                is ResultData.Error -> return@withContext ResultData.Error(titlesByGenre.exception)
                is ResultData.Success -> {
                    val titlesWithRatingData = titlesByGenre.data.results.map {
                        async {
                            populateTitleWithRating(it)
                        }
                    }.awaitAll()
                    return@withContext ResultData.Success(titlesWithRatingData)
                }
            }
        }
    }

    private suspend fun populateTitleWithRating(title: TitleData): TitleWithRatingRemoteData {
        var titleRating = getTitleRating(title.id)
        titleRating = when (titleRating) {
            is ResultData.Success -> ResultData.Success(
                titleRating.data
            )
            is ResultData.Error -> ResultData.Success(TitleData.Rating.DEFAULT_VALUE)
        }
        return TitleWithRatingRemoteData(
            title.id,
            title.primaryImage,
            title.titleText,
            title.releaseDate,
            titleRating.data
        )
    }

    private suspend fun getTitleRating(titleId: String): ResultData<TitleData.Rating> {
        val titleRating = titleRemoteDataSource.getTitleRating(titleId)
        return when (titleRating) {
            is ResultData.Error -> ResultData.Error(titleRating.exception)
            is ResultData.Success -> ResultData.Success(titleRating.data.results)
        }
    }
}