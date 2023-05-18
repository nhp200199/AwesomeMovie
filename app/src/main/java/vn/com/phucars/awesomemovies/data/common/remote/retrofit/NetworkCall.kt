package vn.com.phucars.awesomemovies.data.common.remote.retrofit

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.com.phucars.awesomemovies.data.BaseNetworkData
import vn.com.phucars.awesomemovies.data.common.remote.NetworkResponse
import java.io.IOException

class NetworkCall<T: Any>(
    private val delegate: Call<BaseNetworkData<T>>
) : Call<NetworkResponse<BaseNetworkData<T>>> {
    override fun enqueue(callback: Callback<NetworkResponse<BaseNetworkData<T>>>) {
        return delegate.enqueue(object : Callback<BaseNetworkData<T>> {
            override fun onResponse(call: Call<BaseNetworkData<T>>, response: Response<BaseNetworkData<T>>) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()

                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(this@NetworkCall, Response.success(NetworkResponse.Success(
                            body
                        )))
                    } else {
                        callback.onResponse(this@NetworkCall, Response.success(NetworkResponse.UnknownError))
                    }
                } else {
                    if (error != null) {
                        callback.onResponse(this@NetworkCall, Response.success(NetworkResponse.ApiError(error, code)))
                    } else {
                        callback.onResponse(this@NetworkCall, Response.success(NetworkResponse.UnknownError))
                    }
                }
            }

            override fun onFailure(call: Call<BaseNetworkData<T>>, t: Throwable) {
                val networkResponse = when (t) {
                    is IOException -> NetworkResponse.NetworkError(t)
                    else -> NetworkResponse.UnknownError
                }
                callback.onResponse(this@NetworkCall, Response.success(networkResponse))
            }

        })
    }

    override fun clone(): Call<NetworkResponse<BaseNetworkData<T>>> {
        return NetworkCall(delegate.clone())
    }

    override fun execute(): Response<NetworkResponse<BaseNetworkData<T>>> = throw UnsupportedOperationException("MyCall doesn't support execute()")

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled() = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}