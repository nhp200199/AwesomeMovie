package vn.com.phucars.awesomemovies.data.title.source.remote

import vn.com.phucars.awesomemovies.data.BaseNetworkData
import vn.com.phucars.awesomemovies.data.BaseNetworkPagingData
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.common.exception.UnknownException
import vn.com.phucars.awesomemovies.data.common.remote.NetworkResponse
import vn.com.phucars.awesomemovies.data.title.TitleData
import vn.com.phucars.awesomemovies.data.title.source.remote.TitleRemoteDataSource
import vn.com.phucars.awesomemovies.data.title.source.remote.TitleService
import javax.inject.Inject

class TitleRemoteDataSourceImpl @Inject constructor(private val titleService: TitleService) :
    TitleRemoteDataSource {
    override suspend fun getTitleListByGenre(
        genre: String,
        page: Int
    ): ResultData<BaseNetworkPagingData<List<TitleData>>> =
        when (val titleListResult = titleService.getTitleListByGenre(genre, page)) {
            is NetworkResponse.ApiError -> ResultData.Error(UnknownException())
            is NetworkResponse.NetworkError -> ResultData.Error(UnknownException())
            is NetworkResponse.Success -> ResultData.Success(titleListResult.body)
            NetworkResponse.UnknownError -> ResultData.Error(UnknownException())
        }

    override suspend fun getTitleById(id: String): ResultData<BaseNetworkData<TitleData>> =
        when (val titleData = titleService.getTitleById(id)) {
            is NetworkResponse.ApiError -> ResultData.Error(UnknownException())
            is NetworkResponse.NetworkError -> ResultData.Error(UnknownException())
            is NetworkResponse.Success -> ResultData.Success(titleData.body)
            NetworkResponse.UnknownError -> ResultData.Error(UnknownException())
        }

    override suspend fun getTitleRating(titleId: String): ResultData<BaseNetworkData<TitleData.Rating>> =
        when (val ratingResult = titleService.getTitleRating(titleId)) {
            is NetworkResponse.ApiError -> ResultData.Error(UnknownException())
            is NetworkResponse.NetworkError -> ResultData.Error(UnknownException())
            is NetworkResponse.Success -> ResultData.Success(ratingResult.body)
            NetworkResponse.UnknownError -> ResultData.Error(UnknownException())
        }

    override suspend fun getGenres(): ResultData<BaseNetworkData<List<String?>>> {
        return when(val genreListResult = titleService.getGenreList()) {
            is NetworkResponse.ApiError -> ResultData.Error(UnknownException())
            is NetworkResponse.NetworkError -> ResultData.Error(UnknownException())
            is NetworkResponse.Success -> ResultData.Success(genreListResult.body)
            NetworkResponse.UnknownError -> ResultData.Error(UnknownException())
        }
    }
}