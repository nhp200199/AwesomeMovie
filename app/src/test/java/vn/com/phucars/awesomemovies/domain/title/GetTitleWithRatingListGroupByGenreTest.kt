package vn.com.phucars.awesomemovies.domain.title

import io.mockk.coEvery
import io.mockk.mockk
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
import vn.com.phucars.awesomemovies.testdata.TitleDomainTest

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class GetTitleWithRatingListGroupByGenreTest {
    lateinit var SUT: GetTitleWithRatingListGroupByGenre
    lateinit var titleRepository: TitleRepository

    @Before
    fun setup() {
        titleRepository = mockk<TitleRepository>()
        SUT = GetTitleWithRatingListGroupByGenre(titleRepository)

    }

    @Test
    fun getTitleWithRatingListGroupByGenre_success_successReturned() = runTest {
        coEvery {
            titleRepository.getTitleWithRatingListGroupByGenre()
        }.returns(ResultDomain.Success(TitleDomainTest.TITLE_WITH_RATING_LIST_GROUP_BY_GENRE))

        val result = SUT.getTitleWithRatingListGroupByGenre()

        assertThat(result, `is`(instanceOf(ResultDomain.Success::class.java)))
    }

    @Test
    fun getTitleWithRatingListGroupByGenre_generalError_ErrorReturned() = runTest {
        coEvery {
            titleRepository.getTitleWithRatingListGroupByGenre()
        }.returns(ResultDomain.Error(Exception()))

        val result = SUT.getTitleWithRatingListGroupByGenre()

        assertThat(result, `is`(instanceOf(ResultDomain.Error::class.java)))
    }

}