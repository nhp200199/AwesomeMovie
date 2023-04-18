package vn.com.phucars.awesomemovies.data.title

import kotlinx.coroutines.*
import vn.com.phucars.awesomemovies.data.ResultData

class TitleRepositoryImpl(private val titleRemoteDataSource: TitleRemoteDataSource) : TitleRepository {
    override suspend fun getTitlesByGenre(genre: String): ResultData<List<TitleData>> {
        val titlesByGenre = titleRemoteDataSource.getTitlesByGenre(genre)
        if (titlesByGenre is ResultData.Success) {
            return ResultData.Success(titlesByGenre.data.results)
        } else throw Exception()
    }

    override suspend fun getTitleById(id: String): ResultData<TitleData> {
        val titleById = titleRemoteDataSource.getTitleById(id)
        if (titleById is ResultData.Success) {
            return ResultData.Success(titleById.data.results)
        } else throw Exception()
    }

    override suspend fun getTitleRating(titleId: String): ResultData<TitleData.Rating> {
        val titleRating = titleRemoteDataSource.getTitleRating(titleId)
        if (titleRating is ResultData.Success) {
            return ResultData.Success(titleRating.data.results)
        } else throw Exception()
    }

    override suspend fun getGenres(): ResultData<List<String?>> {
        val genres = titleRemoteDataSource.getGenres()
        return if (genres is ResultData.Success) {
            ResultData.Success(genres.data.results.filterNotNull())
        } else {
            ResultData.Error((genres as ResultData.Error).exception)
        }
    }

    override suspend fun getTitlesWithRatingByGenre(genre: String): ResultData<List<TitleWithRatingData>> = withContext(Dispatchers.IO) {
        val titlesByGenre = getTitlesByGenre(genre)
        if (titlesByGenre is ResultData.Success) {
            val titleWithRatingDomain = titlesByGenre.data.map {
                getRatingForTitleAsync(this, it)
            }.awaitAll()
            return@withContext ResultData.Success(titleWithRatingDomain)
        } else throw Exception()
    }

    private fun getRatingForTitleAsync(coroutineScope: CoroutineScope, title: TitleData) =
        coroutineScope.async {
            val titleRating = getTitleRating(title.id) as ResultData.Success
            return@async TitleWithRatingData(
                title.id,
                title.primaryImage,
                title.titleText,
                title.releaseDate,
                titleRating.data
            )
        }
}