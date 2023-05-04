package vn.com.phucars.awesomemovies.data.title

import vn.com.phucars.awesomemovies.testdata.TitleDataTest
import vn.com.phucars.awesomemovies.testdata.TitleDomainTest

class TitleLocalDataSourceTd : TitleLocalDataSource {
    var cachedTitlesWithRating: List<TitleWithRatingLocalData>? = null
    var cacheTitleCount = 0
    override suspend fun cacheTitlesWithRating(data: List<TitleWithRatingLocalData>) {
        cachedTitlesWithRating = data
    }

    override suspend fun cacheTitleWithRating(data: TitleWithRatingLocalData) {
        cacheTitleCount++
        if (cachedTitlesWithRating == null) {
            cachedTitlesWithRating = TitleDataTest.TITLE_WITH_RATING_LOCAL_LIST_DATA
        }
        val indexOfFirst = cachedTitlesWithRating!!.indexOfFirst { it.id == data.id }
        val toMutableList = cachedTitlesWithRating!!.toMutableList()
        toMutableList[indexOfFirst] = data
        cachedTitlesWithRating = toMutableList
    }
}