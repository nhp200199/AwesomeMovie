package vn.com.phucars.awesomemovies.data.title

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.title.source.remote.TitleByGenrePagingDataSource
import vn.com.phucars.awesomemovies.data.title.source.remote.TitleBySearchPagingDataSource
import vn.com.phucars.awesomemovies.data.title.source.remote.TitleRemoteDataSource
import vn.com.phucars.awesomemovies.dispatcher.DispatcherProvider
import vn.com.phucars.awesomemovies.domain.title.TitleDomain
import vn.com.phucars.awesomemovies.mapper.Mapper
import javax.inject.Inject

class TitleRepositoryImpl @Inject constructor(
    private val titleRemoteDataSource: TitleRemoteDataSource,
//    private val titleLocalDataSource: TitleLocalDataSource,
    private val titleDataDtoToDomain: Mapper<TitleData, TitleDomain>,
    private val dispatcherProvider: DispatcherProvider
//    private val titleWithRatingDomainToLocalDto: Mapper<TitleWithRatingDomain, TitleWithRatingLocalData>,
//    private val titleWithRatingListDomainToLocalDto: ListMapper<TitleWithRatingDomain, TitleWithRatingLocalData>,
//    private val titleWithRatingLocalDtoToDomain: ListMapper<TitleWithRatingLocalData, TitleWithRatingDomain>
) : TitleRepository {
    override suspend fun getTitleById(
        id: String,
        info: String?
    ): ResultData<TitleDomain> {
        val titleDetailResult = withContext(dispatcherProvider.io()) {
            if (info != null) {
                titleRemoteDataSource.getTitleDetailById(id, info)
            } else {
                titleRemoteDataSource.getTitleDetailById(id)
            }
        }

        return when (titleDetailResult) {
            is ResultData.Error -> ResultData.Error(
                titleDetailResult.exception
            )
            is ResultData.Success -> ResultData.Success(
                titleDataDtoToDomain.map(
                    titleDetailResult.data.results
                )
            )
        }
    }

    override suspend fun searchForString(searchString: String, sortOptions: Map<String, String?>): ResultData<List<TitleDomain>> {
        val searchForTitle = titleRemoteDataSource.searchForTitle(searchString, sortOptions = sortOptions)
        return when(searchForTitle) {
            is ResultData.Error -> ResultData.Error(Exception())
            is ResultData.Success -> ResultData.Success(
                searchForTitle.data.results.map {
                    titleDataDtoToDomain.map(it)
                }
            )
        }
    }

    override suspend fun getTitlePagingListBySearch(
        searchString: String,
        sortOptions: Map<String, String?>
    ): Flow<PagingData<TitleDomain>> {
        return Pager(
            PagingConfig(10, enablePlaceholders = false)
        ) {
            TitleBySearchPagingDataSource(
                titleRemoteDataSource,
                searchString,
                sortOptions,
            )
        }.flow.map {
            it.map {
                titleDataDtoToDomain.map(it)
            }
        }
    }

    override suspend fun getGenres(): ResultData<List<String>> {
        val genres = titleRemoteDataSource.getGenres()
        return if (genres is ResultData.Success) {
            ResultData.Success(genres.data.results.filterNotNull())
        } else {
            ResultData.Error((genres as ResultData.Error).exception)
        }
    }

    override suspend fun getTitlePagingListByGenre(genre: String): Flow<PagingData<TitleDomain>> {
        return Pager(
            PagingConfig(10, enablePlaceholders = false)
        ) {
            TitleByGenrePagingDataSource(
                titleRemoteDataSource,
                genre
            )
        }.flow.map {
            it.map {
                titleDataDtoToDomain.map(it)
            }
        }
    }
}