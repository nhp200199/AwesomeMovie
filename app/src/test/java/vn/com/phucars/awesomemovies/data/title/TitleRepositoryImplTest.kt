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
import org.junit.Test
import vn.com.phucars.awesomemovies.data.BaseNetworkData
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.domain.ResultDomain
import vn.com.phucars.awesomemovies.domain.title.TitleWithRatingDomain
import vn.com.phucars.awesomemovies.mapper.ListMapper
import vn.com.phucars.awesomemovies.mapper.Mapper
import vn.com.phucars.awesomemovies.testdata.TitleDataTest
import vn.com.phucars.awesomemovies.testdata.TitleDomainTest

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class TitleRepositoryImplTest {
    lateinit var SUT: TitleRepositoryImpl
    lateinit var titleRemoteDataSource: TitleRemoteDataSource
    val titleLocalDataSourceTd = TitleLocalDataSourceTd()
    lateinit var titleWithRatingRemoteDtoToDomain: Mapper<TitleWithRatingRemoteData, TitleWithRatingDomain>
    lateinit var titleWithRatingDomainToLocalDto: Mapper<TitleWithRatingDomain, TitleWithRatingLocalData>
    lateinit var titleWithRatingListDomainToLocalDto: ListMapper<TitleWithRatingDomain, TitleWithRatingLocalData>

    @Before
    fun setup() {
        titleRemoteDataSource = mockk<TitleRemoteDataSource>()
        titleWithRatingRemoteDtoToDomain =
            mockk<Mapper<TitleWithRatingRemoteData, TitleWithRatingDomain>>()
        titleWithRatingListDomainToLocalDto =
            mockk<ListMapper<TitleWithRatingDomain, TitleWithRatingLocalData>>()
        titleWithRatingDomainToLocalDto =
            mockk<Mapper<TitleWithRatingDomain, TitleWithRatingLocalData>>()

        SUT = TitleRepositoryImpl(
            titleRemoteDataSource,
            titleLocalDataSourceTd,
            titleWithRatingRemoteDtoToDomain,
            titleWithRatingDomainToLocalDto,
            titleWithRatingListDomainToLocalDto
        )
    }

    @Test
    fun getGenres_success_genresListWithoutNullValuesReturned() = runTest {
        successGetGenres()

        var genres = SUT.getGenres()

        assertThat(genres, `is`(instanceOf(ResultDomain.Success::class.java)))
        genres = genres as ResultDomain.Success
        assertThat(genres.data.size, `is`(6))
    }

    @Test
    fun getTitleWithRatingById_correctTitleIdPassed() = runTest {
        val slot = slot<String>()
        successGetTitleById()
        successGetTitleRating()
        mapTitleWithRatingRemoteDtoToDomain()
        mapTitleWithRatingDomainToLocalDto()

        SUT.getTitleWithRatingById(TitleDataTest.TITLE_ID)

        coVerify(exactly = 1) {
            titleRemoteDataSource.getTitleById(capture(slot))
        }
        assertThat(slot.captured, `is`(TitleDataTest.TITLE_ID))
    }

    //get title with rating by id -> success -> data returned
    @Test
    fun getTitleWithRatingById_success_titleWithRatingReturned() = runTest {
        successGetTitleById()
        successGetTitleRating()
        mapTitleWithRatingRemoteDtoToDomain()
        mapTitleWithRatingDomainToLocalDto()

        val titleWithRatingById = SUT.getTitleWithRatingById(TitleDataTest.TITLE_ID)

        assertThat(titleWithRatingById, `is`(instanceOf(ResultDomain.Success::class.java)))
    }

    @Test
    fun getTitleWithRatingById_generalErrorWhenGetTitle_generalErrorReturned() = runTest {
        generalErrorGettingTitleById()
        successGetTitleRating()

        val titleWithRatingById = SUT.getTitleWithRatingById(TitleDataTest.TITLE_ID)

        assertThat(titleWithRatingById, `is`(instanceOf(ResultDomain.Error::class.java)))
    }

    @Test
    fun getTitleWithRatingById_generalErrorWhenGetTitleRating_titleWithRatingReturned_ratingDataHasDefaultValue() =
        runTest {
            successGetTitleById()
            generalErrorGetRatingForATitle()
            mapTitleWithDefaultRatingRemoteDtoToDomain()

            val titleWithRatingById =
                SUT.getTitleWithRatingById(TitleDataTest.TITLE_ID) as ResultDomain.Success

            assertThat(titleWithRatingById, `is`(instanceOf(ResultDomain.Success::class.java)))
            assertThat(
                titleWithRatingById.data.averageRating,
                `is`(TitleData.Rating.DEFAULT_VALUE.averageRating)
            )
            assertThat(
                titleWithRatingById.data.numVotes,
                `is`(TitleData.Rating.DEFAULT_VALUE.numVotes)
            )
        }

    @Test
    fun getTitleWithRatingListByGenre_correctGenrePassed() = runTest {
        val genreSlot = slot<String>()
        successGetTitleListByGenre()
        successGetTitleRating()
        mapTitleWithRatingRemoteDtoToDomain()
        mapTitleWithRatingListDomainToLocalDto()

        SUT.getTitleWithRatingListByGenre(TitleDataTest.GENRE_DRAMA)

        coVerify { titleRemoteDataSource.getTitleListByGenre(capture(genreSlot)) }
        assertThat(genreSlot.captured, `is`(TitleDataTest.GENRE_DRAMA))
    }

    @Test
    fun getTitleWithRatingListByGenre_success_titleWithRatingListByGenreReturned() = runTest {
        successGetTitleListByGenre()
        successGetTitleRating()
        mapTitleWithRatingRemoteDtoToDomain()
        mapTitleWithRatingListDomainToLocalDto()

        val titles = SUT.getTitleWithRatingListByGenre(TitleDataTest.GENRE_DRAMA)

        assertThat(titles, `is`(instanceOf(ResultDomain.Success::class.java)))
    }

    @Test
    fun getGenre_generalError_generaErrorReturned() = runTest {
        generalErrorGetGenre()

        val data = SUT.getGenres()

        assertThat(data, `is`(instanceOf(ResultDomain.Error::class.java)))
    }

    @Test
    fun getTitleWithRatingListByGenre_generalErrorWhenGetTitles_generalErrorReturned() = runTest {
        generalErrorGetTitleListByGenre()

        val data = SUT.getTitleWithRatingListByGenre(TitleDataTest.GENRE_DRAMA)

        assertThat(data, `is`(instanceOf(ResultDomain.Error::class.java)))
    }

    @Test
    fun getTitleWithRatingListByGenre_generalErrorWhenGetRatingForATile_titleWithRatingListByGenreReturned_titleWithErrorRatingHaveDefaultRatingValue() =
        runTest {
            successGetTitleListByGenre()
            generalErrorGetRatingForATitle()
            mapTitleWithDefaultRatingRemoteDtoToDomain()
            mapTitleWithDefaultRatingListDomainToLocalDto()

            val titleWithRatingListByGenre =
                SUT.getTitleWithRatingListByGenre(TitleDataTest.GENRE_DRAMA) as ResultDomain.Success

            assertThat(titleWithRatingListByGenre, `is`(instanceOf(ResultDomain.Success::class.java)))
            assertThat(
                titleWithRatingListByGenre.data.size,
                `is`(TitleDataTest.TITLE_WITH_RATING_REMOTE_LIST_DATA.size)
            )
            val idx = titleWithRatingListByGenre.data.findLast { it.id == TitleDataTest.TITLE_ID }
            assertThat(idx!!.averageRating, `is`(TitleData.Rating.DEFAULT_VALUE.averageRating))
            assertThat(idx!!.numVotes, `is`(TitleData.Rating.DEFAULT_VALUE.numVotes))
        }

    private fun mapTitleWithDefaultRatingDomainToLocalDto() {
        every { titleWithRatingDomainToLocalDto.map(TitleDomainTest.TITLE_WITH_DEFAULT_RATING_DOMAIN) }
            .returns(TitleDataTest.TITLE_WITH_DEFAULT_RATING_LOCAL_DATA)
    }


    @Test
    fun getTitleWithRatingListByGenre_success_dataCached() = runTest {
        successGetTitleListByGenre()
        successGetTitleRating()
        mapTitleWithRatingRemoteDtoToDomain()
        mapTitleWithRatingListDomainToLocalDto()

        SUT.getTitleWithRatingListByGenre(TitleDataTest.GENRE_DRAMA)

        assertThat(
            titleLocalDataSourceTd.cachedTitlesWithRating,
            `is`(TitleDataTest.TITLE_WITH_RATING_LOCAL_LIST_DATA)
        )
    }

    @Test
    fun getTitleWithRatingListByGenre_generalErrorGetTitles_noInteractionDataCached() = runTest {
        generalErrorGetTitleListByGenre()

        SUT.getTitleWithRatingListByGenre(TitleDataTest.GENRE_DRAMA)

        assertThat(titleLocalDataSourceTd.cachedTitlesWithRating, `is`(nullValue()))
    }

    @Test
    fun getTitleWithRatingListByGenre_generalErrorGetTitleRating_noInteractionDataCached() = runTest {
        successGetTitleListByGenre()
        generalErrorGetRatingForATitle()
        mapTitleWithDefaultRatingRemoteDtoToDomain()
        mapTitleWithDefaultRatingListDomainToLocalDto()

        SUT.getTitleWithRatingListByGenre(TitleDataTest.GENRE_DRAMA)

        assertThat(
            titleLocalDataSourceTd.cachedTitlesWithRating,
            `is`(TitleDataTest.TITLE_WITH_DEFAULT_RATING_LOCAL_LIST_DATA)
        )
    }

    @Test
    fun getTitleWithRatingById_success_updateTitleInLocal() = runTest {
        successGetTitleById()
        successGetTitleRating(GetType.UPDATE)
        mapTitleWithUpdatedRatingRemoteDtoToDomain()
        mapTitleWithUpdatedRatingDomainToLocalDto()

        SUT.getTitleWithRatingById(TitleDataTest.TITLE_ID) as ResultDomain.Success

        assertThat(
            titleLocalDataSourceTd.cachedTitlesWithRating!![0].rating,
            `is`(TitleDataTest.UPDATE_TITLE_RATING.averageRating)
        )
    }

    @Test
    fun getTitleWithRatingById_generalErrorGetTitle_noUpdateTitleInLocal() = runTest {
        generalErrorGettingTitleById()

        SUT.getTitleWithRatingById(TitleDataTest.TITLE_ID)

        assertThat(
            titleLocalDataSourceTd.cacheTitleCount,
            `is`(0)
        )
    }

    @Test
    fun getTitleWithRatingById_generalErrorGetRating_noUpdateTitleInLocal() = runTest {
        successGetTitleById()
        generalErrorGetRatingForATitle()
        mapTitleWithDefaultRatingRemoteDtoToDomain()

        SUT.getTitleWithRatingById(TitleDataTest.TITLE_ID)

        assertThat(
            titleLocalDataSourceTd.cacheTitleCount,
            `is`(0)
        )
    }

    //offline cases
    //get titles with rating -> success -> titles with rating returned
    //get titles with rating -> success -> titles with rating returned

    //end offline cases

    //helper methods
    private fun mapTitleWithUpdatedRatingRemoteDtoToDomain() {
        every { titleWithRatingRemoteDtoToDomain.map(TitleDataTest.TITLE_WITH_UPDATED_RATING_REMOTE_DATA) }
            .returns(TitleDomainTest.TITLE_WITH_UPDATED_RATING_DOMAIN)
    }

    private fun mapTitleWithRatingDomainToLocalDto() {
        every { titleWithRatingDomainToLocalDto.map(TitleDomainTest.TITLE_WITH_RATING_DOMAIN) }
            .returns(TitleDataTest.TITLE_WITH_RATING_LOCAL_DATA)
    }

    private fun mapTitleWithUpdatedRatingDomainToLocalDto() {
        every { titleWithRatingDomainToLocalDto.map(TitleDomainTest.TITLE_WITH_UPDATED_RATING_DOMAIN) }
            .returns(TitleDataTest.TITLE_WITH_UPDATED_RATING_LOCAL_DATA)
    }

    private fun mapTitleWithDefaultRatingListDomainToLocalDto() {
        every { titleWithRatingListDomainToLocalDto.map(TitleDomainTest.TITLE_WITH_DEFAULT_RATING_LIST_DOMAIN) }
            .returns(TitleDataTest.TITLE_WITH_DEFAULT_RATING_LOCAL_LIST_DATA)
    }

    private fun mapTitleWithRatingListDomainToLocalDto() {
        every { titleWithRatingListDomainToLocalDto.map(TitleDomainTest.TITLE_WITH_RATING_LIST_DOMAIN) }
            .returns(TitleDataTest.TITLE_WITH_RATING_LOCAL_LIST_DATA)
    }

    private fun mapTitleWithDefaultRatingRemoteDtoToDomain() {
        every { titleWithRatingRemoteDtoToDomain.map(TitleDataTest.TITLE_WITH_DEFAULT_RATING_REMOTE_DATA) }
            .returns(TitleDomainTest.TITLE_WITH_DEFAULT_RATING_DOMAIN)
    }

    private fun mapTitleWithRatingRemoteDtoToDomain() {
        every { titleWithRatingRemoteDtoToDomain.map(TitleDataTest.TITLE_WITH_RATING_REMOTE_DATA) }
            .returns(TitleDomainTest.TITLE_WITH_RATING_DOMAIN)
    }

    private fun generalErrorGettingTitleById() {
        coEvery { titleRemoteDataSource.getTitleById(TitleDataTest.TITLE_ID) }
            .returns(ResultData.Error(Exception()))
    }

    private fun generalErrorGetRatingForATitle() {
        coEvery { titleRemoteDataSource.getTitleRating(TitleDataTest.TITLE_ID) }
            .returns(ResultData.Error(Exception()))
    }

    private fun generalErrorGetTitleListByGenre() {
        coEvery { titleRemoteDataSource.getTitleListByGenre(TitleDataTest.GENRE_DRAMA) }
            .returns(ResultData.Error(Exception()))
    }

    private fun generalErrorGetGenre() {
        coEvery { titleRemoteDataSource.getGenres() }
            .returns(ResultData.Error(Exception()))
    }

    private suspend fun successGetTitleById() {
        coEvery { titleRemoteDataSource.getTitleById(TitleDataTest.TITLE_ID) }
            .returns(
                ResultData.Success<BaseNetworkData<TitleData>>(
                    BaseNetworkData(TitleDataTest.TITLE_DATA)
                )
            )
    }

    private suspend fun successGetTitleRating(type: GetType = GetType.INITIAL) {
        coEvery { titleRemoteDataSource.getTitleRating(TitleDataTest.TITLE_ID) }
            .returns(
                ResultData.Success<BaseNetworkData<TitleData.Rating>>(
                    when (type) {
                        GetType.INITIAL -> BaseNetworkData(TitleDataTest.TITLE_RATING)
                        GetType.UPDATE -> BaseNetworkData(TitleDataTest.UPDATE_TITLE_RATING)
                    }
                )
            )
    }

    private suspend fun successGetTitleListByGenre() {
        coEvery { titleRemoteDataSource.getTitleListByGenre(TitleDataTest.GENRE_DRAMA) }
            .returns(
                ResultData.Success<BaseNetworkData<List<TitleData>>>(
                    BaseNetworkData(TitleDataTest.TITLE_LIST_DATA)
                )
            )
    }

    private suspend fun successGetGenres() {
        coEvery { titleRemoteDataSource.getGenres() }
            .returns(ResultData.Success(BaseNetworkData(TitleDataTest.GENRES_LIST)))
    }

    //end helper methods
}