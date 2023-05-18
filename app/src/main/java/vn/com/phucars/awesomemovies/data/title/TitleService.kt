package vn.com.phucars.awesomemovies.data.title

import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import vn.com.phucars.awesomemovies.data.BaseNetworkData
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.common.remote.NetworkResponse

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