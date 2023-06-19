package vn.com.phucars.awesomemovies.data.title

data class DetailTitleRemoteData(
    val id: String,
    val ratingsSummary: TitleData.Rating?,
    val primaryImage: TitleData.PrimaryImage?,
    val principalCast: List<PrincipalCast>,
    val genres: Genre,
    val titleText: TitleData.TitleText,
    val releaseDate: TitleData.ReleaseDate?,
    val runtime: TitleDuration,
    val plot: Plot?
)

data class Plot(
    val plotText: PlotText,
    val trailer: String
) {
    data class PlotText(val plainText: String)

}

data class TitleDuration(val seconds: Int)

data class Genre(
    val genres: List<GenreInfo>
) {
    class GenreInfo(val id: String, val text: String)

}

data class PrincipalCast(
    val credits: List<Credit>
) {


    data class Credit(
        val name: Actor,
        val characters: List<InTitleCharacter>?
    ) {
        data class InTitleCharacter(val name: String)
        class Actor(
            val id: String,
            val nameText: ActorName,
            val primaryImage: ActorPrimaryImage?
        ) {
            class ActorName(val text: String)
            class ActorPrimaryImage(val url: String)
        }
    }

}
