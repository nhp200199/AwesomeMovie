package vn.com.phucars.awesomemovies.ui

sealed class ResultViewState<out R> {

    object Initial : ResultViewState<Nothing>()
    object Loading : ResultViewState<Nothing>()
    data class Success<out T>(val data: T) : ResultViewState<T>()
    data class Error(val exception: Exception) : ResultViewState<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Loading -> "Loading"
            is Initial -> "Initial"
        }
    }
}