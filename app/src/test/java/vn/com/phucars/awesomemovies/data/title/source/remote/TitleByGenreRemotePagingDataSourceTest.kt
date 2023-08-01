package vn.com.phucars.awesomemovies.data.title.source.remote

import androidx.paging.PagingSource
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
import vn.com.phucars.awesomemovies.data.BaseNetworkData
import vn.com.phucars.awesomemovies.data.BaseNetworkPagingData
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.testdata.GenreDataTest
import vn.com.phucars.awesomemovies.testdata.RatingDataTest
import vn.com.phucars.awesomemovies.testdata.TitleDataTest
import java.lang.Exception

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class TitleByGenreRemotePagingDataSourceTest {
    lateinit var SUT: TitleByGenrePagingDataSource
    val titleRemoteDataSource = mockk<TitleRemoteDataSource>()

    @Before
    fun setup() {
        SUT = TitleByGenrePagingDataSource(titleRemoteDataSource, GenreDataTest.GENRE_ACTION)

    }

    @Test
    fun getTitlesPaging_firstLoad_success_pageReturnedWithPreviousKeyIsNull() = runTest {
        val pageToLoad = 1

        coEvery { titleRemoteDataSource.getTitleListByGenre(GenreDataTest.GENRE_ACTION, pageToLoad) }
            .returns(ResultData.Success(BaseNetworkPagingData(
                listOf(TitleDataTest.TITLE_100_YEARS_DATA),
                pageToLoad.toString(),
                pageToLoad.inc().toString()
            )))
        coEvery { titleRemoteDataSource.getTitleRating(TitleDataTest.TITLE_100_YEARS_ID) }
            .returns(ResultData.Success(BaseNetworkData(RatingDataTest.TITLE_100_YEARS_RATING)))

        assertThat(
            PagingSource.LoadResult.Page(
                listOf(
                    TitleDataTest.TITLE_100_YEARS_WITH_RATING_REMOTE_DATA
                ), null, pageToLoad + 1
            ), `is`(
                SUT.load(
                    PagingSource.LoadParams.Refresh(
                        key = null,
                        loadSize = 1,
                        placeholdersEnabled = false
                    )
                )
            )
        )

    }

    @Test
    fun getTitlesPaging_secondLoad_pageReturnedWithNormalPreviousAndNextKey() = runTest {
        val pageToLoad = 2

        coEvery { titleRemoteDataSource.getTitleListByGenre(GenreDataTest.GENRE_ACTION, pageToLoad) }
            .returns(ResultData.Success(BaseNetworkPagingData(
                listOf(TitleDataTest.TITLE_100_YEARS_DATA),
                pageToLoad.toString(),
                pageToLoad.inc().toString()
            )))
        coEvery { titleRemoteDataSource.getTitleRating(TitleDataTest.TITLE_100_YEARS_ID) }
            .returns(ResultData.Success(BaseNetworkData(RatingDataTest.TITLE_100_YEARS_RATING)))

        assertThat(
            PagingSource.LoadResult.Page(
                listOf(
                    TitleDataTest.TITLE_100_YEARS_WITH_RATING_REMOTE_DATA
                ), pageToLoad.dec(), pageToLoad.inc()
            ), `is`(
                SUT.load(
                    PagingSource.LoadParams.Append(
                        key = pageToLoad,
                        loadSize = 1,
                        placeholdersEnabled = false
                    )
                )
            )
        )
    }

    @Test
    fun getTitlesPaging_endLoad_pageReturnedWithNullNextKey() = runTest {
        val pageToLoad = BaseRemotePagingDataSource.MAX_PAGE

        coEvery { titleRemoteDataSource.getTitleListByGenre(GenreDataTest.GENRE_ACTION, pageToLoad) }
            .returns(ResultData.Success(BaseNetworkPagingData(
                listOf(TitleDataTest.TITLE_100_YEARS_DATA),
                pageToLoad.toString(),
                "null"
            )))
        coEvery { titleRemoteDataSource.getTitleRating(TitleDataTest.TITLE_100_YEARS_ID) }
            .returns(ResultData.Success(BaseNetworkData(RatingDataTest.TITLE_100_YEARS_RATING)))

        assertThat(
            PagingSource.LoadResult.Page(
                listOf(
                    TitleDataTest.TITLE_100_YEARS_WITH_RATING_REMOTE_DATA
                ), pageToLoad - 1, null
            ), `is`(
                SUT.load(
                    PagingSource.LoadParams.Append(
                        key = pageToLoad,
                        loadSize = 1,
                        placeholdersEnabled = false
                    )
                )
            )
        )
    }

    @Test
    fun getTitlePaging_errorGettingTitles_pageWithErrorReturned() = runTest {
        coEvery { titleRemoteDataSource.getTitleListByGenre(GenreDataTest.GENRE_ACTION, BaseRemotePagingDataSource.MAX_PAGE) }
            .returns(ResultData.Error(Exception()))

        assertThat(
            SUT.load(
                PagingSource.LoadParams.Append(
                    key = BaseRemotePagingDataSource.MAX_PAGE,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            ), `is`(instanceOf(PagingSource.LoadResult.Error::class.java))

        )
    }
}