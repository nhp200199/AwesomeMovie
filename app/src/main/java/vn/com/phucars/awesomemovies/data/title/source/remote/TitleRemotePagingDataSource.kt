package vn.com.phucars.awesomemovies.data.title.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.title.TitleData
import vn.com.phucars.awesomemovies.data.title.TitleWithRatingRemoteData
import javax.inject.Inject

class TitleRemotePagingDataSource @Inject constructor(
    private val titleRemoteDataSource: TitleRemoteDataSource,
    private val genre: String
) : PagingSource<Int, TitleWithRatingRemoteData>() {

    override fun getRefreshKey(state: PagingState<Int, TitleWithRatingRemoteData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TitleWithRatingRemoteData> {
        val start = params.key ?: 1 //starts at page 1 if no pages are loaded before
        val titleWithRating = withContext(Dispatchers.IO) {
            val titlesByGenre = titleRemoteDataSource.getTitleListByGenre(genre, page = start)
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
        return when(titleWithRating) {
            is ResultData.Error -> LoadResult.Error(titleWithRating.exception)
            is ResultData.Success -> LoadResult.Page(
                titleWithRating.data,
                if (start == MIN_PAGE) null else start - 1,
                if (start == MAX_PAGE) null else start + 1
            )
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

    companion object {
        const val MIN_PAGE = 1
        const val MAX_PAGE = 10
    }
}