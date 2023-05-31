package vn.com.phucars.awesomemovies.data.title.source.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import vn.com.phucars.awesomemovies.data.BaseNetworkData
import vn.com.phucars.awesomemovies.data.common.remote.NetworkResponse
import vn.com.phucars.awesomemovies.data.title.TitleData

interface TitleService {
    @GET("/titles")
    suspend fun getTitleListByGenre(@Query("genre") genre: String): NetworkResponse<BaseNetworkData<List<TitleData>>>

    @GET("/titles/{id}")
    suspend fun getTitleById(@Path("id") id: String): NetworkResponse<BaseNetworkData<TitleData>>

    @GET("/titles/{id}/ratings")
    suspend fun getTitleRating(@Path("id") id: String): NetworkResponse<BaseNetworkData<TitleData.Rating>>

    @GET("/titles/utils/genres")
    suspend fun getGenreList(): NetworkResponse<BaseNetworkData<List<String?>>>
}