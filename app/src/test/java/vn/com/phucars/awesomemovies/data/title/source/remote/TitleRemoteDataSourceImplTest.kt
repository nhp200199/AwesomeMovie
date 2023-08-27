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
    private val pageToLoad = 1
    @Before
    fun setup() {
        titleService = mockk<TitleService>()
        SUT = TitleRemoteDataSourceImpl(titleService)
    }

    @Test
    fun searchForTitle_correctSearchStringPassed() = runTest {
        success()

        val slot = slot<String>()
        SUT.searchForTitle(TitleDataTest.TITLE_SEARCH_STRING)

        coVerify() {
            titleService.searchForTitle(capture(slot), pageToLoad, emptyMap())
        }
        assertThat(slot.captured, `is`(TitleDataTest.TITLE_SEARCH_STRING))
    }

    @Test
    fun searchForTitle_success_successValueReturned() = runTest {
        success()

        val searchForTitle = SUT.searchForTitle(TitleDataTest.TITLE_SEARCH_STRING)

        assertThat(searchForTitle, `is`(instanceOf(ResultData.Success::class.java)))
    }

    @Test
    fun searchForTitle_success_sortYearIncrease_successValueReturned() = runTest {
        successWithYearSortedIncr()

        val searchForTitle = SUT.searchForTitle(TitleDataTest.TITLE_SEARCH_STRING
            , sortOptions = mapOf(
                SORT to SORT_YEAR_INCR
            )
        )

        assertThat(searchForTitle, `is`(instanceOf(ResultData.Success::class.java)))
        assertThat((searchForTitle as ResultData.Success).data.results, `is`(
            listOf(
                TitleDataTest.TITLE_100_YEARS_DATA,
                TitleDataTest.TITLE_CUONG_DATA,
            )
        ))
    }

    @Test
    fun searchForTitle_success_sortYearDecrease_successValueReturned() = runTest {
        successWithYearSortedDecr()

        val searchForTitle = SUT.searchForTitle(TitleDataTest.TITLE_SEARCH_STRING,
            sortOptions = mapOf(
                SORT to SORT_YEAR_DECR
            ))

        assertThat(searchForTitle, `is`(instanceOf(ResultData.Success::class.java)))
        assertThat((searchForTitle as ResultData.Success).data.results, `is`(
            listOf(
                TitleDataTest.TITLE_CUONG_DATA,
                TitleDataTest.TITLE_100_YEARS_DATA,
            )
        ))
    }

    @Test
    fun searchForTitle_networkError_networkErrorReturned() = runTest {
        networkError()

        val searchForTitle = SUT.searchForTitle(TitleDataTest.TITLE_SEARCH_STRING)

        assertThat(searchForTitle, `is`(instanceOf(ResultData.Error::class.java)))
    }

    @Test
    fun searchForTitle_apiError_apiErrorReturned() = runTest {
        apiError()

        val searchForTitle = SUT.searchForTitle(TitleDataTest.TITLE_SEARCH_STRING)

        assertThat(searchForTitle, `is`(instanceOf(ResultData.Error::class.java)))
    }

    @Test
    fun searchForTitle_unknownError_unknownErrorReturned() = runTest {
        unknownError()

        val searchForTitle = SUT.searchForTitle(TitleDataTest.TITLE_SEARCH_STRING)

        assertThat(searchForTitle, `is`(instanceOf(ResultData.Error::class.java)))
    }

    private fun success() {
        coEvery {
            titleService.searchForTitle(
                TitleDataTest.TITLE_SEARCH_STRING,
                pageToLoad,
                emptyMap()
            )
        }.returns(
            NetworkResponse.Success(
                BaseNetworkPagingData(
                    listOf(
                        TitleDataTest.TITLE_100_YEARS_DATA
                    ), "", ""
                )
            )
        )
    }

    private fun successWithYearSortedIncr() {
        coEvery {
            titleService.searchForTitle(
                TitleDataTest.TITLE_SEARCH_STRING,
                pageToLoad,
                mapOf(
                    SORT to SORT_YEAR_INCR
                )
            )
        }.returns(
            NetworkResponse.Success(
                BaseNetworkPagingData(
                    listOf(
                        TitleDataTest.TITLE_100_YEARS_DATA,
                        TitleDataTest.TITLE_CUONG_DATA
                    ), "", ""
                )
            )
        )
    }

    private fun successWithYearSortedDecr() {
        coEvery {
            titleService.searchForTitle(
                TitleDataTest.TITLE_SEARCH_STRING,
                pageToLoad,
                mapOf(
                    SORT to SORT_YEAR_DECR
                )
            )
        }.returns(
            NetworkResponse.Success(
                BaseNetworkPagingData(
                    listOf(
                        TitleDataTest.TITLE_CUONG_DATA,
                        TitleDataTest.TITLE_100_YEARS_DATA,
                    ), "", ""
                )
            )
        )
    }

    private fun networkError() {
        coEvery {
            titleService.searchForTitle(
                TitleDataTest.TITLE_SEARCH_STRING,
                pageToLoad,
                emptyMap()
            )
        }.returns(
            NetworkResponse.NetworkError(IOException())
        )
    }

    private fun apiError() {
        coEvery {
            titleService.searchForTitle(
                TitleDataTest.TITLE_SEARCH_STRING,
                pageToLoad,
                emptyMap()
            )
        }.returns(
            NetworkResponse.ApiError()
        )
    }

    private fun unknownError() {
        coEvery {
            titleService.searchForTitle(
                TitleDataTest.TITLE_SEARCH_STRING,
                pageToLoad,
                emptyMap()
            )
        }.returns(
            NetworkResponse.UnknownError
        )
    }
}
