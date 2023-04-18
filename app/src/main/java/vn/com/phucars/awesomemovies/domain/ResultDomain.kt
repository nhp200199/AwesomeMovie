package vn.com.phucars.awesomemovies.domain

sealed class ResultDomain<out R> {

    data class Success<out T>(val data: T) : ResultDomain<T>()
    data class Error(val exception: Exception) : ResultDomain<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}