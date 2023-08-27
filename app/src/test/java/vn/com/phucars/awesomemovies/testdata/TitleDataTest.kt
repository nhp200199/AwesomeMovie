package vn.com.phucars.awesomemovies.testdata

import vn.com.phucars.awesomemovies.data.title.*
import vn.com.phucars.awesomemovies.testdata.RatingDataTest.Companion.TITLE_100_YEARS_RATING
import vn.com.phucars.awesomemovies.testdata.RatingDataTest.Companion.TITLE_CUONG_RATING
import vn.com.phucars.awesomemovies.testdata.RatingDataTest.Companion.TITLE_DUNG_RATING
import vn.com.phucars.awesomemovies.testdata.RatingDataTest.Companion.TITLE_PHUC_RATING

class TitleDataTest {
    companion object {
        val TITLE_SEARCH_STRING = "Fast & Furious"
        
        val TITLE_100_YEARS_ID = "tt0001922"
        val TITLE_CUONG_ID = "tt0001923"
        val TITLE_DUNG_ID = "tt0001924"
        val TITLE_PHUC_ID = "tt0001925"

        val TITLE_IMAGE = PrimaryImage("https://m.media-amazon.com/images/M/MV5BMWY3YWY1OTktNjc3Ni00NThiLWI0ODYtOTNjM2E4YjQ2MmJkXkEyXkFqcGdeQXVyMjcyMzI2OTQ@._V1_.jpg")

        val PRINCIPAL_CAST_MCCOY = PrincipalCast(
            listOf(
                PrincipalCast.Credit(
                    PrincipalCast.Credit.Actor(
                        "123",
                        PrincipalCast.Credit.Actor.ActorName("Gertrude McCoy"),
                        PrincipalCast.Credit.Actor.ActorPrimaryImage("https://m.media-amazon.com/images/M/MV5BOTRiYzY0NDktOGUwNS00OWRjLWI2YTAtODhkMjc4MThjODdjL2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyMzI5NDcxNzI@._V1_.jpg")
                    ),
                    listOf(
                        PrincipalCast.Credit.InTitleCharacter("Winsome Winnie")
                    )
                )
            )
        )

        val TITLE_GENRES = Genre(
            listOf(
                Genre.GenreInfo("Comedy", "Comedy"),
                Genre.GenreInfo("Short", "Short")
            )
        )
        val TITLE_TEXT = TitleText("100 Years")
        val TITLE_RELEASE_DATE = ReleaseDate(18, 11, 2115)
        val TITLE_CUONG_RELEASE_DATE = ReleaseDate(18, 11, 2117)
        val TITLE_DURATION = TitleDuration(10)
        val TITLE_PLOT_TEXT = Plot(Plot.PlotText("100 years description"), "100 years trailer url")

        val TITLE_100_YEARS_DATA = TitleData(
            TITLE_100_YEARS_ID,
            TITLE_100_YEARS_RATING,
            TITLE_IMAGE,
            listOf(
                PRINCIPAL_CAST_MCCOY
            ),
            TITLE_GENRES,
            TITLE_TEXT,
            TITLE_RELEASE_DATE,
            TITLE_DURATION,
            TITLE_PLOT_TEXT
        )

        val TITLE_CUONG_DATA = TitleData(
            TITLE_CUONG_ID,
            TITLE_CUONG_RATING,
            TITLE_IMAGE,
            listOf(
                PRINCIPAL_CAST_MCCOY
            ),
            TITLE_GENRES,
            TITLE_TEXT,
            TITLE_CUONG_RELEASE_DATE,
            TITLE_DURATION,
            TITLE_PLOT_TEXT
        )

        val TITLE_DUNG_DATA = TitleData(
            TITLE_DUNG_ID,
            TITLE_DUNG_RATING,
            TITLE_IMAGE,
            listOf(
                PRINCIPAL_CAST_MCCOY
            ),
            TITLE_GENRES,
            TITLE_TEXT,
            TITLE_RELEASE_DATE,
            TITLE_DURATION,
            TITLE_PLOT_TEXT
        )

        val TITLE_PHUC_DATA = TitleData(
            TITLE_PHUC_ID,
            TITLE_PHUC_RATING,
            TITLE_IMAGE,
            listOf(
                PRINCIPAL_CAST_MCCOY
            ),
            TITLE_GENRES,
            TITLE_TEXT,
            TITLE_RELEASE_DATE,
            TITLE_DURATION,
            TITLE_PLOT_TEXT
        )


        val TITLE_100_YEARS_DATA_WITH_NULL_RATING = TITLE_100_YEARS_DATA.copy(
            ratingsSummary = null
        )
    }
}

class GenreDataTest {
    companion object {
        val GENRE_ACTION = "action"
        val GENRE_DRAMA = "drama"

        val GENRES_LIST = listOf(
            null,
            GENRE_ACTION,
            GENRE_DRAMA,
        )
    }
}

class RatingDataTest {
    companion object {
        val TITLE_100_YEARS_RATING = Rating(
            4.6f,
            50
        )

        val UPDATE_TITLE_100_YEARS_RATING = Rating(
            4.9f,
            52
        )

        val TITLE_CUONG_RATING = Rating(
            4.7f,
            55
        )
        val TITLE_DUNG_RATING = Rating(
            4.8f,
            52
        )
        val TITLE_PHUC_RATING = Rating(
            4.9f,
            53
        )
    }
}