package vn.com.phucars.awesomemovies.data.title

data class TitleWithRatingData(
    val id: String,
    val primaryImage: TitleData.PrimaryImage,
    val titleText: TitleData.TitleText,
    val releaseDate: TitleData.ReleaseDate,
    val rating: TitleData.Rating
)
