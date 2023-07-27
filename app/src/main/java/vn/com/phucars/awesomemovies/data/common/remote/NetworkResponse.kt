package vn.com.phucars.awesomemovies.data.common.remote

import okhttp3.ResponseBody
import java.io.IOException

sealed class NetworkResponse<out T : Any> {
    /**
     * Success response with body
     */
    data class Success<T : Any>(val body: T) : NetworkResponse<T>()

    /**
     * Failure response with body
     */
    data class ApiError(val body: ResponseBody? = null, val code: Int? = null) : NetworkResponse<Nothing>()

    /**
     * Network error
     */
    data class NetworkError(val error: IOException) : NetworkResponse<Nothing>()

    /**
     * For example, json parsing error
     */
    object UnknownError : NetworkResponse<Nothing>()
}
