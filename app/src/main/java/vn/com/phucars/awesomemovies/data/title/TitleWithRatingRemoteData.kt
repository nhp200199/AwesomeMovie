package vn.com.phucars.awesomemovies.data.title

data class TitleWithRatingRemoteData(
    val id: String,
    val primaryImage: TitleData.PrimaryImage?,
    val titleText: TitleData.TitleText,
    val releaseDate: TitleData.ReleaseDate?,
    val rating: TitleData.Rating?
)
