package vn.com.phucars.awesomemovies.data.title.source.remote

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.hamcrest.CoreMatchers.*
import org.junit.Test
import vn.com.phucars.awesomemovies.data.BaseNetworkPagingData
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.common.remote.NetworkResponse
import vn.com.phucars.awesomemovies.testdata.TitleDataTest
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class TitleRemoteDataSourceImplTest {
    lateinit var SUT: TitleRemoteDataSourceImpl
    lateinit var titleService: TitleService

    @Before
    fun setup() {
        titleService = mockk<TitleService>()
        SUT = TitleRemoteDataSourceImpl(titleService)
    }


    //searchTitle -> check correct search string passed
    @Test
    fun searchForTitle_correctSearchStringPassed() = runTest {
        success()

        val slot = slot<String>()
        SUT.searchForTitle(TitleDataTest.TITLE_SEARCH_STRING)

        coVerify() {
            titleService.searchForTitle(capture(slot))
        }
        assertThat(slot.captured, `is`(TitleDataTest.TITLE_SEARCH_STRING))
    }

    //searchTitle -> success -> return success value
    @Test
    fun searchForTitle_success_successValueReturned() = runTest {
        success()

        val searchForTitle = SUT.searchForTitle(TitleDataTest.TITLE_SEARCH_STRING)

        assertThat(searchForTitle, `is`(instanceOf(ResultData.Success::class.java)))
    }
    //searchTitle -> network fail -> return network fail value
    @Test
    fun searchForTitle_networkError_networkErrorReturned() = runTest {
        networkError()

        val searchForTitle = SUT.searchForTitle(TitleDataTest.TITLE_SEARCH_STRING)

        assertThat(searchForTitle, `is`(instanceOf(ResultData.Error::class.java)))
    }
    //searchTitle -> api error -> return api fail value
    @Test
    fun searchForTitle_apiError_apiErrorReturned() = runTest {
        apiError()

        val searchForTitle = SUT.searchForTitle(TitleDataTest.TITLE_SEARCH_STRING)

        assertThat(searchForTitle, `is`(instanceOf(ResultData.Error::class.java)))
    }

    //searchTitle -> general error -> return general fail value
    @Test
    fun searchForTitle_unknownError_unknownErrorReturned() = runTest {
        unknownError()

        val searchForTitle = SUT.searchForTitle(TitleDataTest.TITLE_SEARCH_STRING)

        assertThat(searchForTitle, `is`(instanceOf(ResultData.Error::class.java)))
    }

    private fun success() {
        coEvery {
            titleService.searchForTitle(TitleDataTest.TITLE_SEARCH_STRING)
        }.returns(
            NetworkResponse.Success(
                BaseNetworkPagingData(
                    listOf(
                        TitleDataTest.DETAIL_TITLE_100_YEARS_DATA
                    ), "", ""
                )
            )
        )
    }

    private fun networkError() {
        coEvery {
            titleService.searchForTitle(TitleDataTest.TITLE_SEARCH_STRING)
        }.returns(
            NetworkResponse.NetworkError(IOException())
        )
    }

    private fun apiError() {
        coEvery {
            titleService.searchForTitle(TitleDataTest.TITLE_SEARCH_STRING)
        }.returns(
            NetworkResponse.ApiError()
        )
    }

    private fun unknownError() {
        coEvery {
            titleService.searchForTitle(TitleDataTest.TITLE_SEARCH_STRING)
        }.returns(
            NetworkResponse.UnknownError
        )
    }
}
