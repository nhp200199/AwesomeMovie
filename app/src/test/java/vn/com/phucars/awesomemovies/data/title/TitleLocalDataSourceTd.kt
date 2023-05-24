package vn.com.phucars.awesomemovies.data.title

import vn.com.phucars.awesomemovies.data.title.source.local.TitleLocalDataSource

class TitleLocalDataSourceTd : TitleLocalDataSource {
    var cachedTitlesWithRating: List<TitleWithRatingLocalData> = emptyList()
    var cacheTitleCount = 0
    override suspend fun cacheTitlesWithRating(data: List<TitleWithRatingLocalData>) {
        cachedTitlesWithRating = data
    }

    override suspend fun cacheTitleWithRating(data: TitleWithRatingLocalData) {
        cacheTitleCount++
        val indexOfFirst = cachedTitlesWithRating.indexOfFirst { it.id == data.id }
        val toMutableList = cachedTitlesWithRating.toMutableList()
        toMutableList[indexOfFirst] = data
        cachedTitlesWithRating = toMutableList
    }

    override suspend fun getTitleWithRatingListByGenre(genre: String): List<TitleWithRatingLocalData> {
        return cachedTitlesWithRating
    }
}