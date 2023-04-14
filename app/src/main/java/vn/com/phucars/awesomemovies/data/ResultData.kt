package vn.com.phucars.awesomemovies.data

sealed class ResultData<out R> {

    data class Success<out T>(val data: T) : ResultData<T>()
    data class Error(val exception: Exception) : ResultData<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}