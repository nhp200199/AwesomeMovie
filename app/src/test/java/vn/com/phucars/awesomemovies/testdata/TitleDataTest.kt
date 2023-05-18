package vn.com.phucars.awesomemovies.testdata

import vn.com.phucars.awesomemovies.data.title.TitleData
import vn.com.phucars.awesomemovies.data.title.TitleWithRatingLocalData
import vn.com.phucars.awesomemovies.data.title.TitleWithRatingRemoteData
import vn.com.phucars.awesomemovies.testdata.RatingDataTest.Companion.TITLE_100_YEARS_RATING
import vn.com.phucars.awesomemovies.testdata.RatingDataTest.Companion.TITLE_CUONG_RATING
import vn.com.phucars.awesomemovies.testdata.RatingDataTest.Companion.TITLE_DUNG_RATING
import vn.com.phucars.awesomemovies.testdata.RatingDataTest.Companion.TITLE_PHUC_RATING
import vn.com.phucars.awesomemovies.testdata.RatingDataTest.Companion.UPDATE_TITLE_100_YEARS_RATING

class TitleDataTest {
    companion object {
        val TITLE_100_YEARS_ID = "tt0001922"
        val TITLE_CUONG_ID = "tt0001923"
        val TITLE_DUNG_ID = "tt0001924"
        val TITLE_PHUC_ID = "tt0001925"

        val TITLE_100_YEARS_DATA = TitleData(
            TITLE_100_YEARS_ID,
            TitleData.PrimaryImage("https://m.media-amazon.com/images/M/MV5BMWY3YWY1OTktNjc3Ni00NThiLWI0ODYtOTNjM2E4YjQ2MmJkXkEyXkFqcGdeQXVyMjcyMzI2OTQ@._V1_.jpg"),
            TitleData.TitleText("100 Years"),
            TitleData.ReleaseDate(18, 11, 2115)
        )
        val TITLE_CUONG_DATA = TitleData(
            TITLE_CUONG_ID,
            TitleData.PrimaryImage("CUONG IMAGE"),
            TitleData.TitleText("CUONG"),
            TitleData.ReleaseDate(18, 11, 2115)
        )
        val TITLE_DUNG_DATA = TitleData(
            TITLE_DUNG_ID,
            TitleData.PrimaryImage("DUNG IMAGE"),
            TitleData.TitleText("DUNG"),
            TitleData.ReleaseDate(18, 11, 2115)
        )
        val TITLE_PHUC_DATA = TitleData(
            TITLE_PHUC_ID,
            TitleData.PrimaryImage("PHUC IMAGE"),
            TitleData.TitleText("PHUC"),
            TitleData.ReleaseDate(18, 11, 2115)
        )
        val TITLE_GENRE_DRAMA_LIST_DATA = listOf(
            TITLE_100_YEARS_DATA,
            TITLE_CUONG_DATA,
        )

        val TITLE_GENRE_ACTION_LIST_DATA = listOf(
            TITLE_DUNG_DATA,
            TITLE_PHUC_DATA,
        )

        val TITLE_100_YEARS_WITH_RATING_REMOTE_DATA = TitleWithRatingRemoteData(
            TITLE_100_YEARS_DATA.id,
            TITLE_100_YEARS_DATA.primaryImage,
            TITLE_100_YEARS_DATA.titleText,
            TITLE_100_YEARS_DATA.releaseDate,
            TITLE_100_YEARS_RATING
        )
        val TITLE_CUONG_WITH_RATING_REMOTE_DATA = TitleWithRatingRemoteData(
            TITLE_CUONG_DATA.id,
            TITLE_CUONG_DATA.primaryImage,
            TITLE_CUONG_DATA.titleText,
            TITLE_CUONG_DATA.releaseDate,
            TITLE_CUONG_RATING
        )
        val TITLE_DUNG_WITH_RATING_REMOTE_DATA = TitleWithRatingRemoteData(
            TITLE_DUNG_DATA.id,
            TITLE_DUNG_DATA.primaryImage,
            TITLE_DUNG_DATA.titleText,
            TITLE_DUNG_DATA.releaseDate,
            TITLE_DUNG_RATING
        )
        val TITLE_PHUC_WITH_RATING_REMOTE_DATA = TitleWithRatingRemoteData(
            TITLE_PHUC_DATA.id,
            TITLE_PHUC_DATA.primaryImage,
            TITLE_PHUC_DATA.titleText,
            TITLE_PHUC_DATA.releaseDate,
            TITLE_PHUC_RATING
        )

        val TITLE_100_YEARS_WITH_DEFAULT_RATING_REMOTE_DATA = TitleWithRatingRemoteData(
            TITLE_100_YEARS_DATA.id,
            TITLE_100_YEARS_DATA.primaryImage,
            TITLE_100_YEARS_DATA.titleText,
            TITLE_100_YEARS_DATA.releaseDate,
            TitleData.Rating.DEFAULT_VALUE
        )

        val TITLE_WITH_RATING_REMOTE_LIST_DATA = listOf(
            TITLE_100_YEARS_WITH_DEFAULT_RATING_REMOTE_DATA,
            TITLE_CUONG_WITH_RATING_REMOTE_DATA
        )

        val TITLE_100_YEARS_WITH_RATING_LOCAL_DATA = TitleWithRatingLocalData(
            TITLE_100_YEARS_ID,
            TITLE_100_YEARS_DATA.primaryImage!!.url,
            TITLE_100_YEARS_DATA.titleText.text,
            "${TITLE_100_YEARS_DATA.releaseDate.day}-${TITLE_100_YEARS_DATA.releaseDate.month}-${TITLE_100_YEARS_DATA.releaseDate.year}",
            TITLE_100_YEARS_RATING.averageRating,
            TITLE_100_YEARS_RATING.numVotes
        )

        val TITLE_CUONG_WITH_RATING_LOCAL_DATA = TitleWithRatingLocalData(
            TITLE_CUONG_ID,
            TITLE_CUONG_DATA.primaryImage!!.url,
            TITLE_CUONG_DATA.titleText.text,
            "${TITLE_CUONG_DATA.releaseDate.day}-${TITLE_CUONG_DATA.releaseDate.month}-${TITLE_CUONG_DATA.releaseDate.year}",
            TITLE_100_YEARS_RATING.averageRating,
            TITLE_100_YEARS_RATING.numVotes
        )

        val TITLE_100_YEARS_WITH_DEFAULT_RATING_LOCAL_DATA = TitleWithRatingLocalData(
            TITLE_100_YEARS_ID,
            TITLE_100_YEARS_DATA.primaryImage!!.url,
            TITLE_100_YEARS_DATA.titleText.text,
            "${TITLE_100_YEARS_DATA.releaseDate.day}-${TITLE_100_YEARS_DATA.releaseDate.month}-${TITLE_100_YEARS_DATA.releaseDate.year}",
            TitleData.Rating.DEFAULT_VALUE.averageRating,
            TitleData.Rating.DEFAULT_VALUE.numVotes
        )

        val TITLE_WITH_RATING_LOCAL_LIST_DATA = listOf(
            TITLE_100_YEARS_WITH_RATING_LOCAL_DATA
        )

        val TITLE_WITH_DEFAULT_RATING_LOCAL_LIST_DATA = listOf(
            TITLE_100_YEARS_WITH_DEFAULT_RATING_LOCAL_DATA,
            TITLE_CUONG_WITH_RATING_LOCAL_DATA
        )

        val TITLE_100_YEARS_WITH_UPDATED_RATING_LOCAL_DATA: TitleWithRatingLocalData = TitleWithRatingLocalData(
            TITLE_100_YEARS_ID,
            TITLE_100_YEARS_DATA.primaryImage!!.url,
            TITLE_100_YEARS_DATA.titleText.text,
            "${TITLE_100_YEARS_DATA.releaseDate.day}-${TITLE_100_YEARS_DATA.releaseDate.month}-${TITLE_100_YEARS_DATA.releaseDate.year}",
            UPDATE_TITLE_100_YEARS_RATING.averageRating,
            UPDATE_TITLE_100_YEARS_RATING.numVotes
        )

        val TITLE_100_YEARS_WITH_UPDATED_RATING_REMOTE_DATA = TitleWithRatingRemoteData(
            TITLE_100_YEARS_DATA.id,
            TITLE_100_YEARS_DATA.primaryImage,
            TITLE_100_YEARS_DATA.titleText,
            TITLE_100_YEARS_DATA.releaseDate,
            UPDATE_TITLE_100_YEARS_RATING
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
        val TITLE_100_YEARS_RATING = TitleData.Rating(
            4.6f,
            50
        )

        val UPDATE_TITLE_100_YEARS_RATING = TitleData.Rating(
            4.9f,
            52
        )

        val TITLE_CUONG_RATING = TitleData.Rating(
            4.7f,
            55
        )
        val TITLE_DUNG_RATING = TitleData.Rating(
            4.8f,
            52
        )
        val TITLE_PHUC_RATING = TitleData.Rating(
            4.9f,
            53
        )
    }
}