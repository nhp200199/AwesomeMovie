package vn.com.phucars.awesomemovies.data.title

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.mockito.Mockito.*
import org.hamcrest.MatcherAssert.*
import org.mockito.kotlin.*


import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

import org.hamcrest.CoreMatchers.*
import org.junit.Rule
import org.junit.Test
import vn.com.phucars.awesomemovies.CoroutineTestRule
import vn.com.phucars.awesomemovies.data.BaseNetworkData
import vn.com.phucars.awesomemovies.data.BaseNetworkPagingData
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.title.source.remote.TitleRemoteDataSource
import vn.com.phucars.awesomemovies.dispatcher.DispatcherProvider
import vn.com.phucars.awesomemovies.domain.ResultDomain
import vn.com.phucars.awesomemovies.domain.title.TitleDomain
import vn.com.phucars.awesomemovies.mapper.ListMapper
import vn.com.phucars.awesomemovies.mapper.Mapper
import vn.com.phucars.awesomemovies.testdata.GenreDataTest
import vn.com.phucars.awesomemovies.testdata.TitleDataTest
import vn.com.phucars.awesomemovies.testdata.TitleDomainTest

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class TitleRepositoryImplTest {
    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    lateinit var SUT: TitleRepositoryImpl
    lateinit var titleRemoteDataSource: TitleRemoteDataSource
    val titleLocalDataSourceTd = TitleLocalDataSourceTd()
    lateinit var titleWithRatingDomainToLocalDto: Mapper<TitleDomain, TitleWithRatingLocalData>
    lateinit var titleWithRatingListDomainToLocalDto: ListMapper<TitleDomain, TitleWithRatingLocalData>
    lateinit var titleWithRatingListLocalToDomain: ListMapper<TitleWithRatingLocalData, TitleDomain>
    lateinit var titleRemoteDtoToDomain: Mapper<TitleData, TitleDomain>
    val dispatcherProvider: DispatcherProvider = coroutineTestRule.testDispatcherProvider

    @Before
    fun setup() {
        titleRemoteDataSource = mockk<TitleRemoteDataSource>()

        titleWithRatingListDomainToLocalDto =
            mockk<ListMapper<TitleDomain, TitleWithRatingLocalData>>()
        titleWithRatingDomainToLocalDto =
            mockk<Mapper<TitleDomain, TitleWithRatingLocalData>>()
        titleWithRatingListLocalToDomain = mockk<ListMapper<TitleWithRatingLocalData, TitleDomain>>()
        titleRemoteDtoToDomain = mockk<Mapper<TitleData, TitleDomain>>()

        SUT = TitleRepositoryImpl(
            titleRemoteDataSource,
//            titleLocalDataSourceTd,
            titleRemoteDtoToDomain,
            dispatcherProvider
//            titleWithRatingDomainToLocalDto,
//            titleWithRatingListDomainToLocalDto,
//            titleWithRatingListLocalToDomain
        )
    }

    @Test
    fun searchForTitle_correctSearchStringPassed() = runTest {
        successSearchForTitle()
        mapTitleRemoteDtoToDomain()

        val slot = slot<String>()
        SUT.searchForString(TitleDataTest.TITLE_SEARCH_STRING)

        coVerify {
            titleRemoteDataSource.searchForTitle(capture(slot))
        }

        assertThat(TitleDataTest.TITLE_SEARCH_STRING, `is`(slot.captured))
    }

    @Test
    fun searchForTitle_success_successValueReturned() = runTest {
        successSearchForTitle()
        mapTitleRemoteDtoToDomain()

        val searchForString = SUT.searchForString(TitleDataTest.TITLE_SEARCH_STRING)

        assertThat(searchForString, `is`(instanceOf(ResultDomain.Success::class.java)))
    }

    @Test
    fun searchForTitle_error_generalErrorReturned() = runTest {
        coEvery {
            titleRemoteDataSource.searchForTitle(TitleDataTest.TITLE_SEARCH_STRING)
        }.returns(
            ResultData.Error(Exception())
        )
        
        val searchForString = SUT.searchForString(TitleDataTest.TITLE_SEARCH_STRING)

        assertThat(searchForString, `is`(instanceOf(ResultDomain.Error::class.java)))
    }

    @Test
    fun getTitleById_correctIdAndCustomInfoPassed() = runTest {
        val idSlot = slot<String>()
        val infoSlot = slot<String>()

        successGetTitleData()
        mapTitleRemoteDtoToDomain()

        SUT.getTitleById(TitleDataTest.TITLE_100_YEARS_ID, TitleRemoteDataSource.ParamInfo.CUSTOM_INFO)

        coVerify(exactly = 1) {
            titleRemoteDataSource.getTitleDetailById(capture(idSlot), capture(infoSlot))
        }
        assertThat(idSlot.captured, `is`(TitleDataTest.TITLE_100_YEARS_ID))
        assertThat(infoSlot.captured, `is`(TitleRemoteDataSource.ParamInfo.CUSTOM_INFO))
    }


    @Test
    fun getTitleById_paramInfoNotPassed_correctIdAndDefaultInfoPassed() = runTest {
        val idSlot = slot<String>()
        val infoSlot = slot<String>()

        successGetTitleData()
        mapTitleRemoteDtoToDomain()

        SUT.getTitleById(TitleDataTest.TITLE_100_YEARS_ID)

        coVerify(exactly = 1) {
            titleRemoteDataSource.getTitleDetailById(capture(idSlot), capture(infoSlot))
        }
        assertThat(idSlot.captured, `is`(TitleDataTest.TITLE_100_YEARS_ID))
        assertThat(infoSlot.captured, `is`(TitleRemoteDataSource.ParamInfo.MINI_INFO))
    }

    @Test
    fun getTitleById_success_detailTitleReturned() = runTest {
        successGetTitleData()
        mapTitleRemoteDtoToDomain()

        val titleById = SUT.getTitleById(TitleDataTest.TITLE_100_YEARS_ID)

        assertThat(titleById, `is`(instanceOf(ResultDomain.Success::class.java)))
    }

    @Test
    fun getTitleById_success_ratingIsNull_titleDetailWithDefaultRatingReturned() = runTest {
        successGetTitleDataWithNullRating()
        mapTitleWithNullRatingRemoteDtoToDomain()

        var titleById = SUT.getTitleById(TitleDataTest.TITLE_100_YEARS_ID)

        assertThat(titleById, `is`(instanceOf(ResultDomain.Success::class.java)))
        titleById = titleById as ResultDomain.Success
        assertThat(titleById.data.averageRating, `is`(Rating.DEFAULT_VALUE.aggregateRating))
        assertThat(titleById.data.numVotes, `is`(Rating.DEFAULT_VALUE.voteCount))
    }

    @Test
    fun getTitleById_generalError_generalErrorReturned() = runTest {
        generalErrorGettingTitle()

        var titleById = SUT.getTitleById(TitleDataTest.TITLE_100_YEARS_ID)

        assertThat(
            titleById,
            `is`(instanceOf(ResultDomain.Error::class.java))
        )
    }

    private fun generalErrorGettingTitle() {
        coEvery { titleRemoteDataSource.getTitleDetailById(TitleDataTest.TITLE_100_YEARS_ID) }
            .returns(ResultData.Error(Exception()))
    }

    @Test
    fun getGenres_success_genresListWithoutNullValuesReturned() = runTest {
        successGetGenres()

        var genres = SUT.getGenres()

        assertThat(genres, `is`(instanceOf(ResultDomain.Success::class.java)))
        genres = genres as ResultDomain.Success
        assertThat(genres.data.size, `is`(GenreDataTest.GENRES_LIST.filterNotNull().size))
    }

    @Test
    fun getGenre_generalError_generaErrorReturned() = runTest {
        generalErrorGetGenre()

        val data = SUT.getGenres()

        assertThat(data, `is`(instanceOf(ResultDomain.Error::class.java)))
    }

    //helper methods
    private fun successSearchForTitle() {
        coEvery {
            titleRemoteDataSource.searchForTitle("Fast & Furious")
        }.returns(
            ResultData.Success(
                BaseNetworkPagingData(
                    listOf(TitleDataTest.TITLE_100_YEARS_DATA),
                    "",
                    ""
                )
            )
        )
    }

    private fun mapTitleRemoteDtoToDomain() {
        every { titleRemoteDtoToDomain.map(TitleDataTest.TITLE_100_YEARS_DATA) }
            .returns(TitleDomainTest.TITLE_100_YEARS_DOMAIN)
    }

    private fun mapTitleWithNullRatingRemoteDtoToDomain() {
        every { titleRemoteDtoToDomain.map(TitleDataTest.TITLE_100_YEARS_DATA_WITH_NULL_RATING) }
            .returns(TitleDomainTest.TITLE_100_YEARS_WITH_DEFAULT_RATING_DOMAIN)
    }

    private fun successGetTitleDataWithNullRating() {
        coEvery { titleRemoteDataSource.getTitleDetailById(TitleDataTest.TITLE_100_YEARS_ID) }
            .returns(
                ResultData.Success(
                    BaseNetworkData(
                        TitleDataTest.TITLE_100_YEARS_DATA_WITH_NULL_RATING
                    )
                )
            )
    }

    private fun successGetTitleData() {
        val slot = slot<String>()
        coEvery { titleRemoteDataSource.getTitleDetailById(TitleDataTest.TITLE_100_YEARS_ID, capture(slot)) }
            .answers {
                when (slot.captured) {
                    TitleRemoteDataSource.ParamInfo.CUSTOM_INFO -> ResultData.Success(
                        BaseNetworkData(
                            TitleDataTest.TITLE_100_YEARS_DATA
                        )
                    )
                    TitleRemoteDataSource.ParamInfo.MINI_INFO -> ResultData.Success(
                        BaseNetworkData(
                            TitleDataTest.TITLE_100_YEARS_DATA
                        )
                    )
                    else -> throw Exception("not support param: ${slot.captured}")
                }
            }
    }

    private fun generalErrorGetGenre() {
        coEvery { titleRemoteDataSource.getGenres() }
            .returns(ResultData.Error(Exception()))
    }

    private suspend fun successGetGenres() {
        coEvery { titleRemoteDataSource.getGenres() }
            .returns(ResultData.Success(BaseNetworkData(GenreDataTest.GENRES_LIST)))
    }

    //end helper methods
}