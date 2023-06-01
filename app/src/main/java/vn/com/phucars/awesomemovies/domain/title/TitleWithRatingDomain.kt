package vn.com.phucars.awesomemovies.domain.title

import vn.com.phucars.awesomemovies.ui.title.TitleWithRatingViewState

data class TitleWithRatingDomain(
    val id: String,
    val imageUrl: String,
    val titleText: String,
    val releaseDate: String,
    val averageRating: Float,
    val numVotes: Int
)

fun TitleWithRatingDomain.toViewState() = TitleWithRatingViewState(
    this.id,
    this.imageUrl,
    this.titleText,
    this.releaseDate,
    this.averageRating,
    this.numVotes
)
