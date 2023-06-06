package vn.com.phucars.awesomemovies.data

open class BaseNetworkData<T>(
    open val results: T


) {
    override fun toString(): String  = results.toString()
}
