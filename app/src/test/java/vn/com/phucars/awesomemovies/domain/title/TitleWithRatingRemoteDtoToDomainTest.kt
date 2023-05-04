package vn.com.phucars.awesomemovies.domain.title

import org.mockito.Mockito.*
import org.hamcrest.MatcherAssert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.hamcrest.CoreMatchers.*
import org.junit.Test
import vn.com.phucars.awesomemovies.testdata.TitleDataTest
import vn.com.phucars.awesomemovies.testdata.TitleDomainTest

@RunWith(MockitoJUnitRunner::class)
class TitleWithRatingRemoteDtoToDomainTest {
    lateinit var SUT: TitleWithRatingRemoteDtoToDomain

    @Before
    fun setup() {
        SUT = TitleWithRatingRemoteDtoToDomain()
    }

    @Test
    fun map_completeTitleWithRatingDto_completeTitleWithRatingDomainReturned() {
        val result = SUT.map(TitleDataTest.TITLE_WITH_RATING_REMOTE_DATA)

        assertThat(result.id, `is`(TitleDomainTest.TITLE_WITH_RATING_DOMAIN.id))
        assertThat(result.imageUrl, `is`(TitleDomainTest.TITLE_WITH_RATING_DOMAIN.imageUrl))
        assertThat(result.titleText, `is`(TitleDomainTest.TITLE_WITH_RATING_DOMAIN.titleText))
        assertThat(result.releaseDate, `is`(TitleDomainTest.TITLE_WITH_RATING_DOMAIN.releaseDate))
        assertThat(result.numVotes, `is`(TitleDomainTest.TITLE_WITH_RATING_DOMAIN.numVotes))
        assertThat(result.averageRating, `is`(TitleDomainTest.TITLE_WITH_RATING_DOMAIN.averageRating))
    }
}