package vn.com.phucars.awesomemovies.ui.titleDetail

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import vn.com.phucars.awesomemovies.domain.ResultDomain
import vn.com.phucars.awesomemovies.domain.title.GetTitleById
import vn.com.phucars.awesomemovies.testdata.TitleDataTest
import vn.com.phucars.awesomemovies.testdata.TitleDomainTest
import vn.com.phucars.awesomemovies.ui.ResultViewState

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class TitleDetailViewModelTest {
    lateinit var SUT: TitleDetailViewModel
    val getTitleById = mockk<GetTitleById>()


    @Before
    fun setup() {
        // setting up test dispatcher as main dispatcher for coroutines
        Dispatchers.setMain(StandardTestDispatcher())
        SUT = TitleDetailViewModel(getTitleById)
    }

    @Test
    fun init_success_happyPathFlow() = runTest {
        coEvery {
            getTitleById(TitleDataTest.TITLE_100_YEARS_ID)
        }.returns(ResultDomain.Success(TitleDomainTest.TITLE_100_YEARS_DOMAIN))

        SUT.titleDetailFlow.test {
            SUT.init(TitleDataTest.TITLE_100_YEARS_ID)

            assertThat(awaitItem(), `is`(ResultViewState.Initial))
            assertThat(awaitItem(), `is`(ResultViewState.Loading))
            assertThat(awaitItem(), `is`(instanceOf(ResultViewState.Success::class.java)))
        }
    }

    @Test
    fun init_failLoadingDetail_generalErrorFlow() = runTest {
        coEvery {
            getTitleById(TitleDataTest.TITLE_100_YEARS_ID)
        }.returns(ResultDomain.Error(Exception()))

        SUT.titleDetailFlow.test {
            SUT.init(TitleDataTest.TITLE_100_YEARS_ID)

            assertThat(awaitItem(), `is`(ResultViewState.Initial))
            assertThat(awaitItem(), `is`(ResultViewState.Loading))
            assertThat(awaitItem(), `is`(instanceOf(ResultViewState.Error::class.java)))
        }
    }

    @Test
    fun refresh_currentStateIsInitial_stateRemains() = runTest {
        SUT.setTitleDetailState(ResultViewState.Initial)
        SUT.refresh()

        runCurrent()

        assertThat(SUT.titleDetailFlow.value, `is`(ResultViewState.Initial))
    }

    @Test
    fun refresh_currentStateIsLoading_stateRemains() = runTest {
        SUT.setTitleDetailState(ResultViewState.Loading)
        SUT.refresh()

        runCurrent()

        assertThat(SUT.titleDetailFlow.value, `is`(ResultViewState.Loading))
    }

    @Test
    fun refresh_currentStateIsSuccess_stateMovesToLoading() = runTest {
        coEvery {
            getTitleById(TitleDataTest.TITLE_100_YEARS_ID)
        }.returns(ResultDomain.Success(TitleDomainTest.TITLE_100_YEARS_DOMAIN))
        SUT.id =TitleDataTest.TITLE_100_YEARS_ID
        SUT.setTitleDetailState(ResultViewState.Success(TitleDomainTest.TITLE_100_YEARS_DOMAIN.toDetailViewState()))

        SUT.titleDetailFlow.test {
            SUT.refresh()

            assertThat(awaitItem(), `is`(instanceOf(ResultViewState.Success::class.java)))
            assertThat(awaitItem(), `is`(ResultViewState.Loading))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun refresh_currentStateIsFail_stateMovesToLoading() = runTest {
        coEvery {
            getTitleById(TitleDataTest.TITLE_100_YEARS_ID)
        }.returns(ResultDomain.Success(TitleDomainTest.TITLE_100_YEARS_DOMAIN))

        SUT.id =TitleDataTest.TITLE_100_YEARS_ID
        SUT.setTitleDetailState(ResultViewState.Error(Exception()))
        SUT.refresh()

        SUT.titleDetailFlow.test {
            assertThat(awaitItem(), `is`(instanceOf(ResultViewState.Error::class.java)))
            assertThat(awaitItem(), `is`(ResultViewState.Loading))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}