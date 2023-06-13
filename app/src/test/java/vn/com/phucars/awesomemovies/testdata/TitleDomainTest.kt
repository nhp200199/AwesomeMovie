package vn.com.phucars.awesomemovies.testdata

import vn.com.phucars.awesomemovies.data.title.TitleData
import vn.com.phucars.awesomemovies.domain.title.TitleWithRatingDomain

class TitleDomainTest {
    companion object {

        val TITLE_100_YEARS_WITH_RATING_DOMAIN = TitleWithRatingDomain(
            TitleDataTest.TITLE_100_YEARS_ID,
            TitleDataTest.TITLE_100_YEARS_DATA.primaryImage!!.url,
            TitleDataTest.TITLE_100_YEARS_DATA.titleText.text,
            "${TitleDataTest.TITLE_100_YEARS_DATA.releaseDate.day}-${TitleDataTest.TITLE_100_YEARS_DATA.releaseDate.month}-${TitleDataTest.TITLE_100_YEARS_DATA.releaseDate.year}",
            RatingDataTest.TITLE_100_YEARS_RATING.averageRating,
            RatingDataTest.TITLE_100_YEARS_RATING.numVotes
        )

        val TITLE_CUONG_WITH_RATING_DOMAIN = TitleWithRatingDomain(
            TitleDataTest.TITLE_CUONG_ID,
            TitleDataTest.TITLE_CUONG_DATA.primaryImage!!.url,
            TitleDataTest.TITLE_CUONG_DATA.titleText.text,
            "${TitleDataTest.TITLE_CUONG_DATA.releaseDate.day}-${TitleDataTest.TITLE_CUONG_DATA.releaseDate.month}-${TitleDataTest.TITLE_CUONG_DATA.releaseDate.year}",
            RatingDataTest.TITLE_CUONG_RATING.averageRating,
            RatingDataTest.TITLE_CUONG_RATING.numVotes
        )

        val TITLE_DUNG_WITH_RATING_DOMAIN = TitleWithRatingDomain(
            TitleDataTest.TITLE_DUNG_ID,
            TitleDataTest.TITLE_DUNG_DATA.primaryImage!!.url,
            TitleDataTest.TITLE_DUNG_DATA.titleText.text,
            "${TitleDataTest.TITLE_DUNG_DATA.releaseDate.day}-${TitleDataTest.TITLE_DUNG_DATA.releaseDate.month}-${TitleDataTest.TITLE_DUNG_DATA.releaseDate.year}",
            RatingDataTest.TITLE_DUNG_RATING.averageRating,
            RatingDataTest.TITLE_DUNG_RATING.numVotes
        )

        val TITLE_PHUC_WITH_RATING_DOMAIN = TitleWithRatingDomain(
            TitleDataTest.TITLE_PHUC_ID,
            TitleDataTest.TITLE_PHUC_DATA.primaryImage!!.url,
            TitleDataTest.TITLE_PHUC_DATA.titleText.text,
            "${TitleDataTest.TITLE_PHUC_DATA.releaseDate.day}-${TitleDataTest.TITLE_PHUC_DATA.releaseDate.month}-${TitleDataTest.TITLE_PHUC_DATA.releaseDate.year}",
            RatingDataTest.TITLE_PHUC_RATING.averageRating,
            RatingDataTest.TITLE_PHUC_RATING.numVotes
        )

        val TITLE_100_YEARS_WITH_DEFAULT_RATING_DOMAIN = TitleWithRatingDomain(
            TitleDataTest.TITLE_100_YEARS_ID,
            TitleDataTest.TITLE_100_YEARS_DATA.primaryImage!!.url,
            TitleDataTest.TITLE_100_YEARS_DATA.titleText.text,
            "${TitleDataTest.TITLE_100_YEARS_DATA.releaseDate.day}-${TitleDataTest.TITLE_100_YEARS_DATA.releaseDate.month}-${TitleDataTest.TITLE_100_YEARS_DATA.releaseDate.year}",
            TitleData.Rating.DEFAULT_VALUE.averageRating,
            TitleData.Rating.DEFAULT_VALUE.numVotes,
        )

        val NEW_TITLE_100_YEARS_DOMAIN = TitleWithRatingDomain(
            TitleDataTest.TITLE_100_YEARS_ID,
            TitleDataTest.NEW_TITLE_100_YEARS_DATA.primaryImage!!.url,
            TitleDataTest.NEW_TITLE_100_YEARS_DATA.titleText.text,
            "${TitleDataTest.NEW_TITLE_100_YEARS_DATA.releaseDate!!.day}-${TitleDataTest.NEW_TITLE_100_YEARS_DATA.releaseDate!!.month}-${TitleDataTest.NEW_TITLE_100_YEARS_DATA.releaseDate!!.year}",
            TitleDataTest.NEW_TITLE_100_YEARS_DATA.ratingsSummary!!.averageRating,
            TitleDataTest.NEW_TITLE_100_YEARS_DATA.ratingsSummary!!.numVotes,
            TitleDataTest.NEW_TITLE_100_YEARS_DATA.plot!!.plotText.plainText,
            TitleDataTest.NEW_TITLE_100_YEARS_DATA.plot!!.trailer,
            TitleDataTest.NEW_TITLE_100_YEARS_DATA.genres.genres.map { it.text },
            TitleDataTest.NEW_TITLE_100_YEARS_DATA.principalCast,
            TitleDataTest.NEW_TITLE_100_YEARS_DATA.runtime.seconds
        )

        val NEW_TITLE_100_YEARS_WITH_DEFAULT_RATING_DOMAIN = TitleWithRatingDomain(
            TitleDataTest.TITLE_100_YEARS_ID,
            TitleDataTest.NEW_TITLE_100_YEARS_DATA.primaryImage!!.url,
            TitleDataTest.NEW_TITLE_100_YEARS_DATA.titleText.text,
            "${TitleDataTest.NEW_TITLE_100_YEARS_DATA.releaseDate!!.day}-${TitleDataTest.NEW_TITLE_100_YEARS_DATA.releaseDate!!.month}-${TitleDataTest.NEW_TITLE_100_YEARS_DATA.releaseDate!!.year}",
            TitleData.Rating.DEFAULT_VALUE.averageRating,
            TitleData.Rating.DEFAULT_VALUE.numVotes,
            TitleDataTest.NEW_TITLE_100_YEARS_DATA.plot!!.plotText.plainText,
            TitleDataTest.NEW_TITLE_100_YEARS_DATA.plot!!.trailer,
            TitleDataTest.NEW_TITLE_100_YEARS_DATA.genres.genres.map { it.text },
            TitleDataTest.NEW_TITLE_100_YEARS_DATA.principalCast,
            TitleDataTest.NEW_TITLE_100_YEARS_DATA.runtime.seconds
        )

        val TITLE_PHUC_WITH_DEFAULT_RATING_DOMAIN = TitleWithRatingDomain(
            TitleDataTest.TITLE_PHUC_ID,
            TitleDataTest.TITLE_PHUC_DATA.primaryImage!!.url,
            TitleDataTest.TITLE_PHUC_DATA.titleText.text,
            "${TitleDataTest.TITLE_PHUC_DATA.releaseDate.day}-${TitleDataTest.TITLE_PHUC_DATA.releaseDate.month}-${TitleDataTest.TITLE_PHUC_DATA.releaseDate.year}",
            TitleData.Rating.DEFAULT_VALUE.averageRating,
            TitleData.Rating.DEFAULT_VALUE.numVotes,
        )


        val TITLE_WITH_DEFAULT_RATING_LIST_DOMAIN: List<TitleWithRatingDomain> = listOf(
            TITLE_100_YEARS_WITH_DEFAULT_RATING_DOMAIN,
            TITLE_CUONG_WITH_RATING_DOMAIN
        )
        val TITLE_WITH_RATING_LIST_DOMAIN = listOf(
            TITLE_100_YEARS_WITH_RATING_DOMAIN,
            TITLE_CUONG_WITH_RATING_DOMAIN
        )

        val TITLE_100_YEARS_WITH_UPDATED_RATING_DOMAIN = TitleWithRatingDomain(
            TitleDataTest.TITLE_100_YEARS_ID,
            TitleDataTest.TITLE_100_YEARS_DATA.primaryImage!!.url,
            TitleDataTest.TITLE_100_YEARS_DATA.titleText.text,
            "${TitleDataTest.TITLE_100_YEARS_DATA.releaseDate.day}-${TitleDataTest.TITLE_100_YEARS_DATA.releaseDate.month}-${TitleDataTest.TITLE_100_YEARS_DATA.releaseDate.year}",
            RatingDataTest.UPDATE_TITLE_100_YEARS_RATING.averageRating,
            RatingDataTest.UPDATE_TITLE_100_YEARS_RATING.numVotes,
        )

        val TITLE_WITH_RATING_LIST_GROUP_BY_GENRE = mapOf(
            Pair(GenreDataTest.GENRE_DRAMA, listOf(TITLE_100_YEARS_WITH_RATING_DOMAIN, TITLE_CUONG_WITH_RATING_DOMAIN)),
            Pair(GenreDataTest.GENRE_ACTION, listOf(TITLE_DUNG_WITH_RATING_DOMAIN, TITLE_PHUC_WITH_RATING_DOMAIN))
        )
    }
}