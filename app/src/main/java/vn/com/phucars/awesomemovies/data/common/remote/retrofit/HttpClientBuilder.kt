package vn.com.phucars.awesomemovies.data.common.remote.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

class HttpClientBuilder {
    companion object {
        fun default(): OkHttpClient.Builder {
            val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            logging.redactHeader("Authorization")
            logging.redactHeader("Cookie")
            builder.addInterceptor(logging)
            builder.addInterceptor(Interceptor {
                val request: Request =
                    it.request().newBuilder().addHeader("X-RapidAPI-Key", "f10b9997a9msh5c8a998b22b36a6p103ef7jsn9cc88f8feb6c")
                        .addHeader("X-RapidAPI-Host", "moviesdatabase.p.rapidapi.com")
                        .build()
                return@Interceptor it.proceed(request)
            })
            return builder
        }
    }
}