package vn.com.phucars.awesomemovies.di.app

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.com.phucars.awesomemovies.data.common.remote.retrofit.HttpClientBuilder
import vn.com.phucars.awesomemovies.data.common.remote.retrofit.NetworkAdapterFactory
import vn.com.phucars.awesomemovies.data.title.source.remote.TitleService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun retrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://moviesdatabase.p.rapidapi.com")
        .client(HttpClientBuilder.default().build())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(NetworkAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun titleService(retrofit: Retrofit): TitleService = retrofit.create(TitleService::class.java)

    @Provides
    @Singleton
    fun firebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
}