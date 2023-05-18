package vn.com.phucars.awesomemovies.data.title

data class TitleData(
    val id: String,
    val primaryImage: PrimaryImage?,
    val titleText: TitleText,
    val releaseDate: ReleaseDate
) {
    data class PrimaryImage(
        val url: String
    )

    data class TitleText(val text: String)

    data class ReleaseDate(
        val day: Int,
        val month: Int,
        val year: Int
    )

    data class Rating(
        val averageRating: Float,
        val numVotes: Int
    ) {
        companion object {
            val DEFAULT_VALUE: Rating = Rating(0f, 0)
        }
    }
}