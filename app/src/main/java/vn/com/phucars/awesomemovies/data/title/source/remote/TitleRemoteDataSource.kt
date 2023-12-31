package vn.com.phucars.awesomemovies.data.title.source.remote

import vn.com.phucars.awesomemovies.data.BaseNetworkData
import vn.com.phucars.awesomemovies.data.BaseNetworkPagingData
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.title.DetailTitleRemoteData
import vn.com.phucars.awesomemovies.data.title.TitleData

interface TitleRemoteDataSource {

    companion object ParamInfo {
        const val MINI_INFO = "mini_info"
        const val CUSTOM_INFO = "custom_info"
    }

    suspend fun getTitleListByGenre(genre: String, page: Int = 1): ResultData<BaseNetworkPagingData<List<TitleData>>>
    suspend fun getTitleById(id: String): ResultData<BaseNetworkData<TitleData>>
    suspend fun getTitleRating(titleId: String): ResultData<BaseNetworkData<TitleData.Rating>>
    suspend fun getGenres(): ResultData<BaseNetworkData<List<String?>>>

    suspend fun getTitleDetailById(id: String, info: String = ParamInfo.MINI_INFO): ResultData<BaseNetworkData<DetailTitleRemoteData>>
}