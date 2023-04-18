package vn.com.phucars.awesomemovies.data.title

import vn.com.phucars.awesomemovies.data.BaseNetworkData
import vn.com.phucars.awesomemovies.data.ResultData

interface TitleRemoteDataSource {
    suspend fun getTitlesByGenre(genre: String): ResultData<BaseNetworkData<List<TitleData>>>
    suspend fun getTitleById(id: String): ResultData<BaseNetworkData<TitleData>>
    suspend fun getTitleRating(titleId: String): ResultData<BaseNetworkData<TitleData.Rating>>
    suspend fun getGenres(): ResultData<BaseNetworkData<List<String?>>>
}