package vn.com.phucars.awesomemovies.data.common.remote.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.com.phucars.awesomemovies.data.title.TitleService

object MovieApiManager {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://moviesdatabase.p.rapidapi.com")
        .client(HttpClientBuilder.default().build())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(NetworkAdapterFactory())
        .build()

    val titleService: TitleService = retrofit.create(TitleService::class.java)
}