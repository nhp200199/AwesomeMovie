package vn.com.phucars.awesomemovies.data

class BaseNetworkPagingData<T>(
    results: T,
    val page: String,
    val next: String,
) : BaseNetworkData<T>(results) {

}