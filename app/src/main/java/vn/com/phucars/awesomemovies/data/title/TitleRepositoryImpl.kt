package vn.com.phucars.awesomemovies.data.title

import kotlinx.coroutines.*
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.domain.ResultDomain
import vn.com.phucars.awesomemovies.domain.title.TitleWithRatingDomain
import vn.com.phucars.awesomemovies.mapper.ListMapper
import vn.com.phucars.awesomemovies.mapper.Mapper

class TitleRepositoryImpl(
    private val titleRemoteDataSource: TitleRemoteDataSource,
    private val titleLocalDataSource: TitleLocalDataSource,
    private val titleWithRatingRemoteDtoToDomain: Mapper<TitleWithRatingRemoteData, TitleWithRatingDomain>,
    private val titleWithRatingDomainToLocalDto: Mapper<TitleWithRatingDomain, TitleWithRatingLocalData>,
    private val titlesWithRatingDomainToLocalDto: ListMapper<TitleWithRatingDomain, TitleWithRatingLocalData>
) : TitleRepository {
    private suspend fun getTitlesByGenre(genre: String): ResultData<List<TitleData>> {
        val titlesByGenre = titleRemoteDataSource.getTitleListByGenre(genre)
        return when (titlesByGenre) {
            is ResultData.Error -> ResultData.Error(titlesByGenre.exception)
            is ResultData.Success -> ResultData.Success(titlesByGenre.data.results)
        }
    }

    override suspend fun getTitleWithRatingById(id: String): ResultDomain<TitleWithRatingDomain> {
        val titleById = titleRemoteDataSource.getTitleById(id)
        when (titleById) {
            is ResultData.Error -> return ResultDomain.Error(titleById.exception)
            is ResultData.Success -> {
                val titleRating = titleRemoteDataSource.getTitleRating(id)
                when (titleRating) {
                    is ResultData.Success -> {
                        val titleWithRating = titleWithRatingRemoteDtoToDomain.map(
                            TitleWithRatingRemoteData(
                                titleById.data.results.id,
                                titleById.data.results.primaryImage,
                                titleById.data.results.titleText,
                                titleById.data.results.releaseDate,
                                titleRating.data.results
                            )
                        )
                        titleLocalDataSource.cacheTitleWithRating(titleWithRatingDomainToLocalDto.map(titleWithRating))
                        return ResultDomain.Success(titleWithRating)
                    }
                    is ResultData.Error -> return ResultDomain.Success(
                        titleWithRatingRemoteDtoToDomain.map(
                            TitleWithRatingRemoteData(
                                titleById.data.results.id,
                                titleById.data.results.primaryImage,
                                titleById.data.results.titleText,
                                titleById.data.results.releaseDate,
                                TitleData.Rating.DEFAULT_VALUE
                            )
                        )
                    )
                }
            }
        }
    }

    private suspend fun getTitleRating(titleId: String): ResultData<TitleData.Rating> {
        val titleRating = titleRemoteDataSource.getTitleRating(titleId)
        return when (titleRating) {
            is ResultData.Error -> ResultData.Error(titleRating.exception)
            is ResultData.Success -> ResultData.Success(titleRating.data.results)
        }
    }

    override suspend fun getGenres(): ResultDomain<List<String?>> {
        val genres = titleRemoteDataSource.getGenres()
        return if (genres is ResultData.Success) {
            ResultDomain.Success(genres.data.results.filterNotNull())
        } else {
            ResultDomain.Error((genres as ResultData.Error).exception)
        }
    }

    override suspend fun getTitleWithRatingListByGenre(genre: String): ResultDomain<List<TitleWithRatingDomain>> =
        withContext(Dispatchers.IO) {
            val titlesByGenre = getTitlesByGenre(genre)
            when (titlesByGenre) {
                is ResultData.Error -> return@withContext ResultDomain.Error(titlesByGenre.exception)
                is ResultData.Success -> {
                    val titlesWithRatingData = titlesByGenre.data.map {
                        async {
                            var titleRating = getTitleRating(it.id)
                            titleRating = when (titleRating) {
                                is ResultData.Success -> ResultData.Success(titleRating.data)
                                is ResultData.Error -> ResultData.Success(TitleData.Rating.DEFAULT_VALUE)
                            }
                            val titleWithRating = TitleWithRatingRemoteData(
                                it.id,
                                it.primaryImage,
                                it.titleText,
                                it.releaseDate,
                                titleRating.data
                            )
                            return@async titleWithRatingRemoteDtoToDomain.map(titleWithRating)
                        }
                    }.awaitAll()
                    titleLocalDataSource.cacheTitlesWithRating(titlesWithRatingDomainToLocalDto.map(titlesWithRatingData))
                    return@withContext ResultDomain.Success(titlesWithRatingData)
                }
            }
        }
}