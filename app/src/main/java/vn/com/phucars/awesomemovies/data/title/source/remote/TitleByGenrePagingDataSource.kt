package vn.com.phucars.awesomemovies.data.title.source.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.title.TitleData
import javax.inject.Inject

class TitleByGenrePagingDataSource @Inject constructor(
    private val titleRemoteDataSource: TitleRemoteDataSource,
    private val genre: String
) : BaseRemotePagingDataSource<TitleData>() {

    override suspend fun requestData(page: Int): ResultData<List<TitleData>> {
        return withContext(Dispatchers.IO) {
            val titlesByGenre = titleRemoteDataSource.getTitleListByGenre(genre, page = page)
            when (titlesByGenre) {
                is ResultData.Error -> return@withContext ResultData.Error(titlesByGenre.exception)
                is ResultData.Success -> {
                    return@withContext ResultData.Success(titlesByGenre.data.results)
                }
            }
        }
    }
}