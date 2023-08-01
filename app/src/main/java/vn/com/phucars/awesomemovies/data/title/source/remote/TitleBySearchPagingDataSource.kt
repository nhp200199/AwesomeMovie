package vn.com.phucars.awesomemovies.data.title.source.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.title.DetailTitleRemoteData

class TitleBySearchPagingDataSource(
    private val titleRemoteDataSource: TitleRemoteDataSource,
    private val searchString: String
) : BaseRemotePagingDataSource<DetailTitleRemoteData>() {

    override suspend fun requestData(page: Int): ResultData<List<DetailTitleRemoteData>> {
        return withContext(Dispatchers.IO) {
            val titlesByGenre = titleRemoteDataSource.searchForTitle(searchString, page = page)
            when (titlesByGenre) {
                is ResultData.Error -> return@withContext ResultData.Error(titlesByGenre.exception)
                is ResultData.Success -> return@withContext ResultData.Success(titlesByGenre.data.results)
            }
        }
    }
}