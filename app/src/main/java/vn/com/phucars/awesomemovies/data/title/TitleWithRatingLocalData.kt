package vn.com.phucars.awesomemovies.data.title

data class TitleWithRatingLocalData(
    val id: String,
    val imageUrl: String,
    val title: String,
    val releaseDate: String,
    val rating: Float,
    val numVote: Int
)