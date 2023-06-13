package vn.com.phucars.awesomemovies.data.title.source.remote

import vn.com.phucars.awesomemovies.data.BaseNetworkData
import vn.com.phucars.awesomemovies.data.BaseNetworkPagingData
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.title.NewTitleRemoteData
import vn.com.phucars.awesomemovies.data.title.TitleData

interface TitleRemoteDataSource {
    suspend fun getTitleListByGenre(genre: String, page: Int = 1): ResultData<BaseNetworkPagingData<List<TitleData>>>
    suspend fun getTitleById(id: String): ResultData<BaseNetworkData<TitleData>>
    suspend fun getTitleRating(titleId: String): ResultData<BaseNetworkData<TitleData.Rating>>
    suspend fun getGenres(): ResultData<BaseNetworkData<List<String?>>>

    suspend fun getTitleDetailById(id: String, info: String = "mini_info"): ResultData<BaseNetworkData<NewTitleRemoteData>>
}