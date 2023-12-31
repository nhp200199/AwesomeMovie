package vn.com.phucars.awesomemovies.domain.title

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.mockito.Mockito.*
import org.hamcrest.MatcherAssert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.hamcrest.CoreMatchers.*
import org.junit.Test
import vn.com.phucars.awesomemovies.data.title.TitleRepository
import vn.com.phucars.awesomemovies.domain.ResultDomain
import vn.com.phucars.awesomemovies.testdata.GenreDataTest
import vn.com.phucars.awesomemovies.testdata.TitleDataTest
import vn.com.phucars.awesomemovies.testdata.TitleDomainTest

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class GetTitlesWithRatingByGenreTest {
    lateinit var SUT: GetTitlesWithRatingByGenre
    lateinit var repository: TitleRepository

    @Before
    fun setup() {
        repository = mockk<TitleRepository>()
        SUT = GetTitlesWithRatingByGenre(repository)
    }

    @Test
    fun getTitlesWithRatingByGenre_correctGenrePassed() = runTest {
        val genreSlot = slot<String>()
        success()

        SUT.invoke(GenreDataTest.GENRE_DRAMA)

        coVerify { repository.getTitleWithRatingListByGenre(capture(genreSlot)) }
        assertThat(genreSlot.captured, `is`(GenreDataTest.GENRE_DRAMA))
    }

    @Test
    fun getTitlesWithRatingByGenre_success_titlesWithRatingByGenreReturned() = runTest {
        success()

        val titles = SUT.invoke(GenreDataTest.GENRE_DRAMA)

        assertThat(titles, `is`(instanceOf(ResultDomain.Success::class.java)))
    }

    //get titles with rating by genre - get titles fail - return error result
    @Test
    fun getTitlesWithRatingByGenre_generalError_errorResultReturned() = runTest {
        generalError()

        val titles = SUT.invoke(GenreDataTest.GENRE_DRAMA)

        assertThat(titles, `is`(instanceOf(ResultDomain.Error::class.java)))
    }

    private fun success() {
        coEvery { repository.getTitleWithRatingListByGenre(GenreDataTest.GENRE_DRAMA) }
            .returns(ResultDomain.Success(TitleDomainTest.TITLE_WITH_RATING_LIST_DOMAIN))
    }

    private fun generalError() {
        coEvery { repository.getTitleWithRatingListByGenre(GenreDataTest.GENRE_DRAMA) }
            .returns(ResultDomain.Error(Exception()))
    }
}