package vn.com.phucars.awesomemovies.data.title.source.remote

import vn.com.phucars.awesomemovies.data.BaseNetworkData
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.title.TitleData

interface TitleRemoteDataSource {
    suspend fun getTitleListByGenre(genre: String): ResultData<BaseNetworkData<List<TitleData>>>
    suspend fun getTitleById(id: String): ResultData<BaseNetworkData<TitleData>>
    suspend fun getTitleRating(titleId: String): ResultData<BaseNetworkData<TitleData.Rating>>
    suspend fun getGenres(): ResultData<BaseNetworkData<List<String?>>>
}