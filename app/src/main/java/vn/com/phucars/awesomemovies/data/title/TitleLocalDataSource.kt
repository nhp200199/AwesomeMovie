package vn.com.phucars.awesomemovies.data.title

interface TitleLocalDataSource {
    suspend fun cacheTitlesWithRating(data: List<TitleWithRatingLocalData>)
}
