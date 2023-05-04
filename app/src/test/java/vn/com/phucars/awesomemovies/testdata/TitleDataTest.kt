package vn.com.phucars.awesomemovies.testdata

import vn.com.phucars.awesomemovies.data.title.TitleData
import vn.com.phucars.awesomemovies.data.title.TitleWithRatingLocalData
import vn.com.phucars.awesomemovies.data.title.TitleWithRatingRemoteData

class TitleDataTest {
    companion object {
        val TITLE_ID = "tt0001922"

        val TITLE_DATA = TitleData(
            TITLE_ID,
            TitleData.PrimaryImage("https://m.media-amazon.com/images/M/MV5BMWY3YWY1OTktNjc3Ni00NThiLWI0ODYtOTNjM2E4YjQ2MmJkXkEyXkFqcGdeQXVyMjcyMzI2OTQ@._V1_.jpg"),
            TitleData.TitleText("100 Years"),
            TitleData.ReleaseDate(18, 11, 2115)
        )


        val GENRE_DRAMA = "Drama"

        val TITLE_RATING = TitleData.Rating(
            4.6f,
            50
        )

        val UPDATE_TITLE_RATING = TitleData.Rating(
            4.9f,
            52
        )

        val TITLE_LIST_DATA = listOf(
            TITLE_DATA
        )

        val TITLE_WITH_RATING_REMOTE_DATA = TitleWithRatingRemoteData(
            TITLE_DATA.id,
            TITLE_DATA.primaryImage,
            TITLE_DATA.titleText,
            TITLE_DATA.releaseDate,
            TITLE_RATING
        )

        val TITLE_WITH_DEFAULT_RATING_REMOTE_DATA = TitleWithRatingRemoteData(
            TITLE_DATA.id,
            TITLE_DATA.primaryImage,
            TITLE_DATA.titleText,
            TITLE_DATA.releaseDate,
            TitleData.Rating.DEFAULT_VALUE
        )

        val TITLE_WITH_RATING_REMOTE_LIST_DATA = listOf(
            TITLE_WITH_RATING_REMOTE_DATA
        )

        val TITLE_WITH_RATING_LOCAL_DATA = TitleWithRatingLocalData(
            TITLE_ID,
            TITLE_DATA.primaryImage.url,
            TITLE_DATA.titleText.text,
            "${TITLE_DATA.releaseDate.day}-${TITLE_DATA.releaseDate.month}-${TITLE_DATA.releaseDate.year}",
            TITLE_RATING.averageRating,
            TITLE_RATING.numVotes
        )

        val TITLE_WITH_DEFAULT_RATING_LOCAL_DATA = TitleWithRatingLocalData(
            TITLE_ID,
            TITLE_DATA.primaryImage.url,
            TITLE_DATA.titleText.text,
            "${TITLE_DATA.releaseDate.day}-${TITLE_DATA.releaseDate.month}-${TITLE_DATA.releaseDate.year}",
            TitleData.Rating.DEFAULT_VALUE.averageRating,
            TitleData.Rating.DEFAULT_VALUE.numVotes
        )

        val TITLE_WITH_RATING_LOCAL_LIST_DATA = listOf(
            TITLE_WITH_RATING_LOCAL_DATA
        )

        val TITLE_WITH_DEFAULT_RATING_LOCAL_LIST_DATA = listOf(
            TITLE_WITH_DEFAULT_RATING_LOCAL_DATA
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

        val TITLE_WITH_UPDATED_RATING_LOCAL_DATA: TitleWithRatingLocalData = TitleWithRatingLocalData(
            TITLE_ID,
            TITLE_DATA.primaryImage.url,
            TITLE_DATA.titleText.text,
            "${TITLE_DATA.releaseDate.day}-${TITLE_DATA.releaseDate.month}-${TITLE_DATA.releaseDate.year}",
            UPDATE_TITLE_RATING.averageRating,
            UPDATE_TITLE_RATING.numVotes
        )

        val TITLE_WITH_UPDATED_RATING_REMOTE_DATA = TitleWithRatingRemoteData(
            TITLE_DATA.id,
            TITLE_DATA.primaryImage,
            TITLE_DATA.titleText,
            TITLE_DATA.releaseDate,
            UPDATE_TITLE_RATING
        )
    }
}