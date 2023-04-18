package vn.com.phucars.awesomemovies.testdata

import vn.com.phucars.awesomemovies.data.title.TitleData
import vn.com.phucars.awesomemovies.data.title.TitleWithRatingData
import vn.com.phucars.awesomemovies.domain.title.TitleWithRatingDomain

class TitleDomainTest {
    companion object {
        val TITLE_WITH_RATING_LIST_DOMAIN = listOf(
            TitleWithRatingDomain(
                "tt0001922",
                "https://m.media-amazon.com/images/M/MV5BMWY3YWY1OTktNjc3Ni00NThiLWI0ODYtOTNjM2E4YjQ2MmJkXkEyXkFqcGdeQXVyMjcyMzI2OTQ@._V1_.jpg",
                "100 Years",
                "18-11-2115",
                4.5f,
                50
            )
        )
    }
}