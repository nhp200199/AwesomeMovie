package vn.com.phucars.awesomemovies.testdata

import vn.com.phucars.awesomemovies.data.title.Rating
import vn.com.phucars.awesomemovies.domain.title.TitleDomain

class TitleDomainTest {
    companion object {

        val TITLE_100_YEARS_DOMAIN = TitleDomain(
            TitleDataTest.TITLE_100_YEARS_DATA.id,
            TitleDataTest.TITLE_100_YEARS_DATA.primaryImage!!.url,
            TitleDataTest.TITLE_100_YEARS_DATA.titleText!!.text,
            "${TitleDataTest.TITLE_100_YEARS_DATA.releaseDate!!.day}-${TitleDataTest.TITLE_100_YEARS_DATA.releaseDate!!.month}-${TitleDataTest.TITLE_100_YEARS_DATA.releaseDate!!.year}",
            TitleDataTest.TITLE_100_YEARS_DATA.ratingsSummary!!.aggregateRating,
            TitleDataTest.TITLE_100_YEARS_DATA.ratingsSummary!!.voteCount,
            TitleDataTest.TITLE_100_YEARS_DATA.plot!!.plotText!!.plainText!!,
            TitleDataTest.TITLE_100_YEARS_DATA.plot!!.trailer!!,
            TitleDataTest.TITLE_100_YEARS_DATA.genres!!.genres!!.map { it.text!! },
            TitleDataTest.TITLE_100_YEARS_DATA.principalCast!!,
            TitleDataTest.TITLE_100_YEARS_DATA.runtime!!.seconds!!
        )

        val TITLE_CUONG_DOMAIN = TitleDomain(
            TitleDataTest.TITLE_CUONG_DATA.id,
            TitleDataTest.TITLE_CUONG_DATA.primaryImage!!.url,
            TitleDataTest.TITLE_CUONG_DATA.titleText!!.text,
            "${TitleDataTest.TITLE_CUONG_DATA.releaseDate!!.day}-${TitleDataTest.TITLE_CUONG_DATA.releaseDate!!.month}-${TitleDataTest.TITLE_CUONG_DATA.releaseDate!!.year}",
            TitleDataTest.TITLE_CUONG_DATA.ratingsSummary!!.aggregateRating,
            TitleDataTest.TITLE_CUONG_DATA.ratingsSummary!!.voteCount,
            TitleDataTest.TITLE_CUONG_DATA.plot!!.plotText!!.plainText!!,
            TitleDataTest.TITLE_CUONG_DATA.plot!!.trailer!!,
            TitleDataTest.TITLE_CUONG_DATA.genres!!.genres!!.map { it.text!! },
            TitleDataTest.TITLE_CUONG_DATA.principalCast!!,
            TitleDataTest.TITLE_CUONG_DATA.runtime!!.seconds!!
        )

        val TITLE_DUNG_DOMAIN = TitleDomain(
            TitleDataTest.TITLE_DUNG_DATA.id,
            TitleDataTest.TITLE_DUNG_DATA.primaryImage!!.url,
            TitleDataTest.TITLE_DUNG_DATA.titleText!!.text,
            "${TitleDataTest.TITLE_DUNG_DATA.releaseDate!!.day}-${TitleDataTest.TITLE_DUNG_DATA.releaseDate!!.month}-${TitleDataTest.TITLE_DUNG_DATA.releaseDate!!.year}",
            TitleDataTest.TITLE_DUNG_DATA.ratingsSummary!!.aggregateRating,
            TitleDataTest.TITLE_DUNG_DATA.ratingsSummary!!.voteCount,
            TitleDataTest.TITLE_DUNG_DATA.plot!!.plotText!!.plainText!!,
            TitleDataTest.TITLE_DUNG_DATA.plot!!.trailer!!,
            TitleDataTest.TITLE_DUNG_DATA.genres!!.genres!!.map { it.text!! },
            TitleDataTest.TITLE_DUNG_DATA.principalCast!!,
            TitleDataTest.TITLE_DUNG_DATA.runtime!!.seconds!!
        )

        val TITLE_PHUC_DOMAIN = TitleDomain(
            TitleDataTest.TITLE_PHUC_DATA.id,
            TitleDataTest.TITLE_PHUC_DATA.primaryImage!!.url,
            TitleDataTest.TITLE_PHUC_DATA.titleText!!.text,
            "${TitleDataTest.TITLE_PHUC_DATA.releaseDate!!.day}-${TitleDataTest.TITLE_PHUC_DATA.releaseDate!!.month}-${TitleDataTest.TITLE_PHUC_DATA.releaseDate!!.year}",
            TitleDataTest.TITLE_PHUC_DATA.ratingsSummary!!.aggregateRating,
            TitleDataTest.TITLE_PHUC_DATA.ratingsSummary!!.voteCount,
            TitleDataTest.TITLE_PHUC_DATA.plot!!.plotText!!.plainText!!,
            TitleDataTest.TITLE_PHUC_DATA.plot!!.trailer!!,
            TitleDataTest.TITLE_PHUC_DATA.genres!!.genres!!.map { it.text!! },
            TitleDataTest.TITLE_PHUC_DATA.principalCast!!,
            TitleDataTest.TITLE_PHUC_DATA.runtime!!.seconds!!
        )

        val TITLE_100_YEARS_WITH_DEFAULT_RATING_DOMAIN = TITLE_100_YEARS_DOMAIN.copy(
            averageRating = Rating.DEFAULT_VALUE.aggregateRating,
            numVotes = Rating.DEFAULT_VALUE.voteCount
        )
    }
}