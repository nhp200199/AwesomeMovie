package vn.com.phucars.awesomemovies.testdata

import vn.com.phucars.awesomemovies.data.title.TitleData
import vn.com.phucars.awesomemovies.data.title.TitleWithRatingData

class TitleDataTest {
    companion object {
        val TITLE_DATA = TitleData(
            "tt0001922",
            TitleData.PrimaryImage("https://m.media-amazon.com/images/M/MV5BMWY3YWY1OTktNjc3Ni00NThiLWI0ODYtOTNjM2E4YjQ2MmJkXkEyXkFqcGdeQXVyMjcyMzI2OTQ@._V1_.jpg"),
            TitleData.TitleText("100 Years"),
            TitleData.ReleaseDate(18, 11, 2115)
        )

        val TITLE_ID = "tt0001922"

        val GENRE_DRAMA = "Drama"

        val TITLE_RATING = TitleData.Rating(
            4.6f,
            50
        )

        val TITLE_LIST_DATA = listOf(
            TitleData(
                "tt0001922",
                TitleData.PrimaryImage("https://m.media-amazon.com/images/M/MV5BMWY3YWY1OTktNjc3Ni00NThiLWI0ODYtOTNjM2E4YjQ2MmJkXkEyXkFqcGdeQXVyMjcyMzI2OTQ@._V1_.jpg"),
                TitleData.TitleText("100 Years"),
                TitleData.ReleaseDate(18, 11, 2115)
            )        )

        val TITLE_WITH_RATING_LIST_DATA = listOf(
            TitleWithRatingData(
                "tt0001922",
                TitleData.PrimaryImage("https://m.media-amazon.com/images/M/MV5BMWY3YWY1OTktNjc3Ni00NThiLWI0ODYtOTNjM2E4YjQ2MmJkXkEyXkFqcGdeQXVyMjcyMzI2OTQ@._V1_.jpg"),
                TitleData.TitleText("100 Years"),
                TitleData.ReleaseDate(18, 11, 2115),
                TITLE_RATING
            )
        )

        val GENRES_LIST = listOf(
            null,
            "Action",
            "Adult",
            "Adventure",
            "Animation",
            "Biography",
            "Comedy",
        )
    }
}