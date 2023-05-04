package vn.com.phucars.awesomemovies.testdata

import vn.com.phucars.awesomemovies.data.title.TitleData
import vn.com.phucars.awesomemovies.domain.title.TitleWithRatingDomain
import vn.com.phucars.awesomemovies.domain.title.TitleWithRatingRemoteDtoToDomain

class TitleDomainTest {
    val mapper = TitleWithRatingRemoteDtoToDomain()
    companion object {

        val TITLE_WITH_RATING_DOMAIN = TitleWithRatingDomain(
            TitleDataTest.TITLE_ID,
            TitleDataTest.TITLE_DATA.primaryImage.url,
            TitleDataTest.TITLE_DATA.titleText.text,
            "${TitleDataTest.TITLE_DATA.releaseDate.day}-${TitleDataTest.TITLE_DATA.releaseDate.month}-${TitleDataTest.TITLE_DATA.releaseDate.year}",
            TitleDataTest.TITLE_RATING.averageRating,
            TitleDataTest.TITLE_RATING.numVotes
        )

        val TITLE_WITH_DEFAULT_RATING_DOMAIN = TitleWithRatingDomain(
            TitleDataTest.TITLE_ID,
            TitleDataTest.TITLE_DATA.primaryImage.url,
            TitleDataTest.TITLE_DATA.titleText.text,
            "${TitleDataTest.TITLE_DATA.releaseDate.day}-${TitleDataTest.TITLE_DATA.releaseDate.month}-${TitleDataTest.TITLE_DATA.releaseDate.year}",
            TitleData.Rating.DEFAULT_VALUE.averageRating,
            TitleData.Rating.DEFAULT_VALUE.numVotes,
        )


        val TITLE_WITH_DEFAULT_RATING_LIST_DOMAIN: List<TitleWithRatingDomain> = listOf(
            TITLE_WITH_DEFAULT_RATING_DOMAIN
        )
        val TITLE_WITH_RATING_LIST_DOMAIN = listOf(
            TITLE_WITH_RATING_DOMAIN
        )

        val TITLE_WITH_UPDATED_RATING_DOMAIN = TitleWithRatingDomain(
            TitleDataTest.TITLE_ID,
            TitleDataTest.TITLE_DATA.primaryImage.url,
            TitleDataTest.TITLE_DATA.titleText.text,
            "${TitleDataTest.TITLE_DATA.releaseDate.day}-${TitleDataTest.TITLE_DATA.releaseDate.month}-${TitleDataTest.TITLE_DATA.releaseDate.year}",
            TitleDataTest.UPDATE_TITLE_RATING.averageRating,
            TitleDataTest.UPDATE_TITLE_RATING.numVotes,
        )
    }
}