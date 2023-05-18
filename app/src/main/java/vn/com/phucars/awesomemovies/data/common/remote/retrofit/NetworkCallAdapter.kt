package vn.com.phucars.awesomemovies.data.common.remote.retrofit

import retrofit2.Call
import retrofit2.CallAdapter
import vn.com.phucars.awesomemovies.data.BaseNetworkData
import vn.com.phucars.awesomemovies.data.common.remote.NetworkResponse
import java.lang.reflect.Type

class NetworkCallAdapter<T: Any>(private val successType: Type) : CallAdapter<BaseNetworkData<T>, Call<NetworkResponse<BaseNetworkData<T>>>> {
    override fun responseType(): Type = successType

    override fun adapt(call: Call<BaseNetworkData<T>>): Call<NetworkResponse<BaseNetworkData<T>>> {
        return NetworkCall(call)
    }
}