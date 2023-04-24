package vn.com.phucars.awesomemovies.data.title

import kotlinx.coroutines.*
import vn.com.phucars.awesomemovies.data.ResultData

class TitleRepositoryImpl(private val titleRemoteDataSource: TitleRemoteDataSource) : TitleRepository {
    override suspend fun getTitlesByGenre(genre: String): ResultData<List<TitleData>> {
        val titlesByGenre = titleRemoteDataSource.getTitlesByGenre(genre)
        return when(titlesByGenre) {
            is ResultData.Success -> ResultData.Success(titlesByGenre.data.results)
            is ResultData.Error -> ResultData.Error(titlesByGenre.exception)
        }
    }

    override suspend fun getTitleById(id: String): ResultData<TitleData> {
        val titleById = titleRemoteDataSource.getTitleById(id)
        if (titleById is ResultData.Success) {
            return ResultData.Success(titleById.data.results)
        } else throw Exception()
    }

    override suspend fun getTitleRating(titleId: String): ResultData<TitleData.Rating> {
        val titleRating = titleRemoteDataSource.getTitleRating(titleId)
        return when(titleRating) {
            is ResultData.Success -> ResultData.Success(titleRating.data.results)
            is ResultData.Error -> ResultData.Error(titleRating.exception)
        }
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
        when(titlesByGenre) {
            is ResultData.Success -> {
                val titlesWithRatingData = titlesByGenre.data.map {
                    async {
                        var titleRating = getTitleRating(it.id)
                        titleRating = when(titleRating) {
                            is ResultData.Success -> ResultData.Success(titleRating.data)
                            is ResultData.Error -> ResultData.Success(TitleData.Rating.DEFAULT_VALUE)
                        }
                        return@async TitleWithRatingData(
                            it.id,
                            it.primaryImage,
                            it.titleText,
                            it.releaseDate,
                            titleRating.data
                        )
                    }
                }.awaitAll()
                return@withContext ResultData.Success(titlesWithRatingData)
            }
            is ResultData.Error -> return@withContext ResultData.Error(titlesByGenre.exception)
        }
    }
}