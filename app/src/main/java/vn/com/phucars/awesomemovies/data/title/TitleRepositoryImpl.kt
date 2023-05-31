package vn.com.phucars.awesomemovies.data.title

import kotlinx.coroutines.*
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.title.source.remote.TitleRemoteDataSource
import vn.com.phucars.awesomemovies.domain.ResultDomain
import vn.com.phucars.awesomemovies.domain.title.TitleWithRatingDomain
import vn.com.phucars.awesomemovies.mapper.Mapper
import java.util.*
import javax.inject.Inject

class TitleRepositoryImpl @Inject constructor(
    private val titleRemoteDataSource: TitleRemoteDataSource,
//    private val titleLocalDataSource: TitleLocalDataSource,
    private val titleWithRatingRemoteDtoToDomain: Mapper<TitleWithRatingRemoteData, TitleWithRatingDomain>,
//    private val titleWithRatingDomainToLocalDto: Mapper<TitleWithRatingDomain, TitleWithRatingLocalData>,
//    private val titleWithRatingListDomainToLocalDto: ListMapper<TitleWithRatingDomain, TitleWithRatingLocalData>,
//    private val titleWithRatingLocalDtoToDomain: ListMapper<TitleWithRatingLocalData, TitleWithRatingDomain>
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
        return when (titleById) {
            is ResultData.Error -> ResultDomain.Error(titleById.exception)
            is ResultData.Success -> {
                val titleWithRatingRemoteData = populateTitleWithRating(titleById.data.results)
                val titleWithRatingDomain = titleWithRatingRemoteDtoToDomain.map(titleWithRatingRemoteData)
//                if (titleWithRatingRemoteData.rating != TitleData.Rating.DEFAULT_VALUE) {
//                    titleLocalDataSource.cacheTitleWithRating(titleWithRatingDomainToLocalDto.map(titleWithRatingDomain))
//                }
                ResultDomain.Success(titleWithRatingDomain)
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

    override suspend fun getGenres(): ResultDomain<List<String>> {
        val genres = titleRemoteDataSource.getGenres()
        return if (genres is ResultData.Success) {
            ResultDomain.Success(genres.data.results.filterNotNull())
        } else {
            ResultDomain.Error((genres as ResultData.Error).exception)
        }
    }

    override suspend fun getTitleWithRatingListByGenre(genre: String): ResultDomain<List<TitleWithRatingDomain>> =
        withContext(Dispatchers.IO) {
//            val localTitles =
//                titleLocalDataSource.getTitleWithRatingListByGenre(genre)
//            if (localTitles.isNotEmpty()) {
//                return@withContext ResultDomain.Success(titleWithRatingLocalDtoToDomain.map(localTitles))
//            } else {
//                val titlesByGenre = getTitlesByGenre(genre)
//                when (titlesByGenre) {
//                    is ResultData.Error -> return@withContext ResultDomain.Error(titlesByGenre.exception)
//                    is ResultData.Success -> {
//                        val titlesWithRatingData = titlesByGenre.data.map {
//                            async {
//                                val titleWithRating = populateTitleWithRating(it)
//                                titleWithRatingRemoteDtoToDomain.map(titleWithRating)
//                            }
//                        }.awaitAll()
//                        titleLocalDataSource.cacheTitlesWithRating(titleWithRatingListDomainToLocalDto.map(titlesWithRatingData))
//                        return@withContext ResultDomain.Success(titlesWithRatingData)
//                    }
//                }
//            }

            val titlesByGenre = getTitlesByGenre(genre)
            when (titlesByGenre) {
                is ResultData.Error -> return@withContext ResultDomain.Error(titlesByGenre.exception)
                is ResultData.Success -> {
                    val titlesWithRatingData = titlesByGenre.data.map {
                        async {
                            val titleWithRating = populateTitleWithRating(it)
                            titleWithRatingRemoteDtoToDomain.map(titleWithRating)
                        }
                    }.awaitAll()
                    return@withContext ResultDomain.Success(titlesWithRatingData)
                }
            }
        }

    override suspend fun getTitleWithRatingListGroupByGenre(): ResultDomain<Map<String, List<TitleWithRatingDomain>>> {
        return withContext(Dispatchers.IO) {
            val genres = titleRemoteDataSource.getGenres()
            if (genres is ResultData.Success) {
                return@withContext try {
                    coroutineScope {
                        val titleWithRatingListGroupByGenre = genres.data.results.filterNotNull()
                            .map { genre ->
                                async {
                                    val titleListByGenre =
                                        titleRemoteDataSource.getTitleListByGenre(genre)
                                    if (titleListByGenre is ResultData.Success) {
                                        val titleWithRatingListByGenre = titleListByGenre.data.results.map {
                                            async {
                                                val titleWithRatingRemoteData =
                                                    populateTitleWithRating(it)
                                                titleWithRatingRemoteDtoToDomain.map(
                                                    titleWithRatingRemoteData
                                                )
                                            }
                                        }.awaitAll()

                                        Pair(genre, titleWithRatingListByGenre)
                                    } else {
                                        throw (titleListByGenre as ResultData.Error).exception
                                    }
                                }
                            }.awaitAll()
                        return@coroutineScope ResultDomain.Success<Map<String, List<TitleWithRatingDomain>>>(
                            mapOf(*titleWithRatingListGroupByGenre.toTypedArray())
                        )
                    }
                } catch (e: Exception) {
                    return@withContext ResultDomain.Error(e)
                }
            } else {
                return@withContext ResultDomain.Error((genres as ResultData.Error).exception)
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
}