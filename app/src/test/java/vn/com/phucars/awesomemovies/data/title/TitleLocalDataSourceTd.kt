package vn.com.phucars.awesomemovies.data.title

class TitleLocalDataSourceTd : TitleLocalDataSource {
    var cachedTitlesWithRating: List<TitleWithRatingLocalData>? = null

    override suspend fun cacheTitlesWithRating(data: List<TitleWithRatingLocalData>) {
        cachedTitlesWithRating = data
    }
}