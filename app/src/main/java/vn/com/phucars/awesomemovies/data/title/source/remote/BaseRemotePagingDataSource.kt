package vn.com.phucars.awesomemovies.data.title.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import vn.com.phucars.awesomemovies.data.ResultData

abstract class BaseRemotePagingDataSource<T: Any> : PagingSource<Int, T>() {
    abstract suspend fun requestData(page: Int): ResultData<List<T>>

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val start = params.key ?: 1 //starts at page 1 if no pages are loaded before
        val resultData = requestData(start)
        return when(resultData) {
            is ResultData.Error -> LoadResult.Error(resultData.exception)
            is ResultData.Success -> LoadResult.Page(
                resultData.data,
                if (start == MIN_PAGE) null else start - 1,
                if (start == MAX_PAGE) null else start + 1
            )
        }
    }

    companion object {
        const val MIN_PAGE = 1
        const val MAX_PAGE = 10
    }
}