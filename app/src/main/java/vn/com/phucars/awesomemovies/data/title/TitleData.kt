package vn.com.phucars.awesomemovies.data.title

data class TitleData(
    val id: String,
    val ratingsSummary: Rating?,
    val primaryImage: PrimaryImage?,
    val principalCast: List<PrincipalCast>?,
    val genres: Genre?,
    val titleText: TitleText?,
    val releaseDate: ReleaseDate?,
    val runtime: TitleDuration?,
    val plot: Plot?
)

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
    val aggregateRating: Float,
    val voteCount: Int
) {
    companion object {
        val DEFAULT_VALUE: Rating = Rating(0f, 0)
    }
}

data class Plot(
    val plotText: PlotText?,
    val trailer: String?
) {
    data class PlotText(val plainText: String?)

}

data class TitleDuration(val seconds: Int?)

data class Genre(
    val genres: List<GenreInfo>?
) {
    class GenreInfo(val id: String, val text: String?)

}

data class PrincipalCast(
    val credits: List<Credit>?
) {


    data class Credit(
        val name: Actor?,
        val characters: List<InTitleCharacter>?
    ) {
        data class InTitleCharacter(val name: String?)
        class Actor(
            val id: String,
            val nameText: ActorName?,
            val primaryImage: ActorPrimaryImage?
        ) {
            class ActorName(val text: String?)
            class ActorPrimaryImage(val url: String?)
        }
    }

}
