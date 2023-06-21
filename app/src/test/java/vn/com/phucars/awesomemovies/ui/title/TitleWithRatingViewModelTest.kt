package vn.com.phucars.awesomemovies.ui.title

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.mockito.Mockito.*
import org.hamcrest.MatcherAssert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.hamcrest.CoreMatchers.*
import org.junit.Rule
import org.junit.Test
import vn.com.phucars.awesomemovies.StandardTestDispatcherProvider
import vn.com.phucars.awesomemovies.CoroutineTestRule
import vn.com.phucars.awesomemovies.domain.ResultDomain
import vn.com.phucars.awesomemovies.domain.title.GetTitleWithRatingListGroupByGenre
import vn.com.phucars.awesomemovies.testdata.TitleDomainTest
import vn.com.phucars.awesomemovies.ui.ResultViewState

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class TitleWithRatingViewModelTest {
    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    lateinit var SUT: TitleWithRatingViewModel
    lateinit var getTitlesWithRatingByGenre: GetTitleWithRatingListGroupByGenre
    @Before
    fun setup() {
        getTitlesWithRatingByGenre = mockk<GetTitleWithRatingListGroupByGenre>()
        SUT = TitleWithRatingViewModel(getTitlesWithRatingByGenre, StandardTestDispatcherProvider())
    }

    @Test
    fun initialize_successGettingTitleWithRating_emitSuccessViewState() = runTest() {
        success()

        SUT.initialize()
        runCurrent()

        assertThat(SUT.titleWithRatingFlow.value, `is`(instanceOf(ResultViewState.Success::class.java)) )
    }

    @Test
    fun initialize_emitLoadingViewState() = runTest {
        coEvery { getTitlesWithRatingByGenre.getTitleWithRatingListGroupByGenre() }
            .coAnswers {
                //suspend the coroutine to test loading value
                delay(1L)
                ResultDomain.Success(TitleDomainTest.TITLE_WITH_RATING_LIST_GROUP_BY_GENRE)
            }

        SUT.initialize()
        runCurrent()

        assertThat(SUT.titleWithRatingFlow.value, `is`(ResultViewState.Loading))
    }

    @Test
    fun initialize_errorGettingTitleWithRating_emitErrorViewState() = runTest {
        failure()

        SUT.initialize()
        runCurrent()

        assertThat(SUT.titleWithRatingFlow.value, `is`(instanceOf(ResultViewState.Error::class.java)))
    }

    private fun failure() {
        coEvery { getTitlesWithRatingByGenre.getTitleWithRatingListGroupByGenre() }
            .returns(ResultDomain.Error(Exception()))
    }

    private fun success() {
        coEvery { getTitlesWithRatingByGenre.getTitleWithRatingListGroupByGenre() }
            .returns(ResultDomain.Success(TitleDomainTest.TITLE_WITH_RATING_LIST_GROUP_BY_GENRE))
    }
}
