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
import vn.com.phucars.awesomemovies.testdata.TitleDataTest

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class TitleRepositoryImplTest {
    lateinit var SUT: TitleRepositoryImpl
    lateinit var titleRemoteDataSource: TitleRemoteDataSource

    @Before
    fun setup() {
        titleRemoteDataSource = mockk<TitleRemoteDataSource>()
        SUT = TitleRepositoryImpl(titleRemoteDataSource)

    }

    @Test
    fun getGenres_success_genresListWithoutNullValuesReturned() = runTest {
        successGetGenres()

        var genres = SUT.getGenres()

        assertThat(genres, `is`(instanceOf(ResultData.Success::class.java)))
        genres = genres as ResultData.Success
        assertThat(genres.data.size, `is`(6))
    }

    @Test
    fun getTitleWithRatingById_correctTitleIdPassed() = runTest {
        val slot = slot<String>()
        successGetTitleById()
        successGetTitleRating()

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

        val titleWithRatingById = SUT.getTitleWithRatingById(TitleDataTest.TITLE_ID)

        assertThat(titleWithRatingById, `is`(instanceOf(ResultData.Success::class.java)))
    }

    @Test
    fun getTitleWithRatingById_generalErrorWhenGetTitle_generalErrorReturned() = runTest {
        generalErrorGettingTitleById()
        successGetTitleRating()

        val titleWithRatingById = SUT.getTitleWithRatingById(TitleDataTest.TITLE_ID)

        assertThat(titleWithRatingById, `is`(instanceOf(ResultData.Error::class.java)))
    }

    @Test
    fun getTitleWithRatingById_generalErrorWhenGetTitleRating_titleWithRatingReturned_ratingDataHasDefaultValue() = runTest {
        successGetTitleById()
        generalErrorGetRatingForATitle()

        val titleWithRatingById = SUT.getTitleWithRatingById(TitleDataTest.TITLE_ID) as ResultData.Success

        assertThat(titleWithRatingById, `is`(instanceOf(ResultData.Success::class.java)))
        assertThat(titleWithRatingById.data.rating, `is`(TitleData.Rating.DEFAULT_VALUE))
    }

    @Test
    fun getTitlesWithRatingByGenre_correctGenrePassed() = runTest {
        val genreSlot = slot<String>()
        successGetTitlesByGenre()
        successGetTitleRating()

        SUT.getTitlesWithRatingByGenre(TitleDataTest.GENRE_DRAMA)

        coVerify { titleRemoteDataSource.getTitlesByGenre(capture(genreSlot)) }
        assertThat(genreSlot.captured, `is`(TitleDataTest.GENRE_DRAMA))
    }

    @Test
    fun getTitlesWithRatingByGenre_success_titlesWithRatingByGenreReturned() = runTest {
        successGetTitlesByGenre()
        successGetTitleRating()

        val titles = SUT.getTitlesWithRatingByGenre(TitleDataTest.GENRE_DRAMA)

        assertThat(titles, `is`(instanceOf(ResultData.Success::class.java)))
    }

    @Test
    fun getGenre_generalError_generaErrorReturned() = runTest {
        generalErrorGetGenre()

        val data = SUT.getGenres()

        assertThat(data, `is`(instanceOf(ResultData.Error::class.java)))
    }

    @Test
    fun getTitlesWithRatingByGenre_generalErrorWhenGetTitles_generalErrorReturned() = runTest {
        generalErrorGetTitlesByGenre()

        val data = SUT.getTitlesWithRatingByGenre(TitleDataTest.GENRE_DRAMA)

        assertThat(data, `is`(instanceOf(ResultData.Error::class.java)))
    }

    @Test
    fun getTitleWithRatingByGenre_generalErrorWhenGetRatingForATile_titlesWithRatingByGenreReturned_TitleWithErrorRatingHaveDefaultRatingValue() = runTest {
        successGetTitlesByGenre()
        generalErrorGetRatingForATitle()

        val titlesWithRatingByGenre = SUT.getTitlesWithRatingByGenre(TitleDataTest.GENRE_DRAMA) as ResultData.Success

        assertThat(titlesWithRatingByGenre, `is`(instanceOf(ResultData.Success::class.java)))
        assertThat(titlesWithRatingByGenre.data.size, `is`(TitleDataTest.TITLE_WITH_RATING_LIST_DATA.size))
        val idx = titlesWithRatingByGenre.data.findLast { it.id == TitleDataTest.TITLE_ID }
        assertThat(idx!!.rating, `is`(TitleData.Rating.DEFAULT_VALUE))
    }

    //get titles by genre -> success -> store in database

    //get title's rating -> success -> store in database

    //offline cases
    //get titles with rating -> success -> titles with rating returned
    //get titles with rating -> success -> titles with rating returned

    //end offline cases

    //error cases

    //helper methods
    private fun generalErrorGettingTitleById() {
        coEvery { titleRemoteDataSource.getTitleById(TitleDataTest.TITLE_ID) }
            .returns(ResultData.Error(Exception()))
    }

    private fun generalErrorGetRatingForATitle() {
        coEvery { titleRemoteDataSource.getTitleRating(TitleDataTest.TITLE_ID) }
            .returns(ResultData.Error(Exception()))
    }

    private fun generalErrorGetTitlesByGenre() {
        coEvery { titleRemoteDataSource.getTitlesByGenre(TitleDataTest.GENRE_DRAMA) }
            .returns(ResultData.Error(Exception()))
    }

    private fun generalErrorGetGenre() {
        coEvery { titleRemoteDataSource.getGenres() }
            .returns(ResultData.Error(Exception()))
    }

    private suspend fun successGetTitleById() {
        coEvery { titleRemoteDataSource.getTitleById(TitleDataTest.TITLE_ID) }
            .returns(ResultData.Success<BaseNetworkData<TitleData>>(
                BaseNetworkData(TitleDataTest.TITLE_DATA)
            ))
    }

    private suspend fun successGetTitleRating() {
        coEvery { titleRemoteDataSource.getTitleRating(TitleDataTest.TITLE_ID) }
            .returns(ResultData.Success<BaseNetworkData<TitleData.Rating>>(
                BaseNetworkData(TitleDataTest.TITLE_RATING)
            ))
    }

    private suspend fun successGetTitlesByGenre() {
        coEvery { titleRemoteDataSource.getTitlesByGenre(TitleDataTest.GENRE_DRAMA) }
            .returns(ResultData.Success<BaseNetworkData<List<TitleData>>>(
                BaseNetworkData(TitleDataTest.TITLE_LIST_DATA)
            ))
    }

    private suspend fun successGetGenres() {
        coEvery { titleRemoteDataSource.getGenres() }
            .returns(ResultData.Success(BaseNetworkData(TitleDataTest.GENRES_LIST)))
    }

    //end helper methods
}