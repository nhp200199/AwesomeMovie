package vn.com.phucars.awesomemovies.domain.title

data class TitleWithRatingDomain(
    val id: String,
    val imageUrl: String,
    val titleText: String,
    val releaseDate: String,
    val averageRating: Float,
    val numVotes: Int
)
