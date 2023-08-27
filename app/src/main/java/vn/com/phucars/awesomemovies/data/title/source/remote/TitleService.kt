package vn.com.phucars.awesomemovies.data.title.source.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import vn.com.phucars.awesomemovies.data.BaseNetworkData
import vn.com.phucars.awesomemovies.data.BaseNetworkPagingData
import vn.com.phucars.awesomemovies.data.common.remote.NetworkResponse
import vn.com.phucars.awesomemovies.data.title.TitleData
import vn.com.phucars.awesomemovies.data.title.Rating

interface TitleService {
    @GET("/titles?info=base_info")
    suspend fun getTitleListByGenre(
        @Query("genre") genre: String,
        @Query("page") page: Int
    ): NetworkResponse<BaseNetworkPagingData<List<TitleData>>>

    @GET("/titles/{id}/ratings")
    suspend fun getTitleRating(@Path("id") id: String): NetworkResponse<BaseNetworkData<Rating>>

    @GET("/titles/utils/genres")
    suspend fun getGenreList(): NetworkResponse<BaseNetworkData<List<String?>>>

    @GET("/titles/{id}")
    suspend fun getTitleById(@Path("id") id: String, @Query("info") info: String):
            NetworkResponse<BaseNetworkData<TitleData>>

    @GET("/titles/search/title/{searchString}?titleType=movie&exact=false&info=base_info")
    suspend fun searchForTitle(
        @Path("searchString") searchString: String,
        @Query("page") page: Int,
        @QueryMap searchOptions: Map<String, String?>
    ): NetworkResponse<BaseNetworkPagingData<List<TitleData>>>
}