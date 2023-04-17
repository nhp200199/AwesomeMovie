package vn.com.phucars.awesomemovies.data.title

import com.nhaarman.mockitokotlin2.doReturn
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.mockito.Mockito.*
import org.hamcrest.MatcherAssert.*
import org.mockito.kotlin.*


import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

import org.hamcrest.CoreMatchers.*
import org.junit.Test
import org.mockito.ArgumentCaptor
import vn.com.phucars.awesomemovies.data.BaseNetworkData
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.testdata.TitleDataTest

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class TitleRepositoryImplTest {
    lateinit var SUT: TitleRepositoryImpl
    lateinit var titleDataSource: TitleDataSource

    @Before
    fun setup() {
        titleDataSource = mockk<TitleDataSource>()
        SUT = TitleRepositoryImpl(titleDataSource)

    }

    @Test
    fun getTitlesByGenre_success_titleDataWithGivenGenreReturned() = runTest {
        successGetTitlesByGenre()

        val titlesByGenre = SUT.getTitlesByGenre(TitleDataTest.GENRE_DRAMA)

        assertThat(titlesByGenre, `is`(instanceOf(ResultData.Success::class.java)))
    }

    @Test
    fun getTitleById_success_titleDataWithGivenIdReturned() = runTest {
        successGetTitleById()

        val titleById = SUT.getTitleById(TitleDataTest.TITLE_ID)

        assertThat(titleById, `is`(instanceOf(ResultData.Success::class.java)))
    }

    @Test
    fun getTitleRating_success_titleRatingReturned() = runTest {
        successGetTitleRating()

        val titleRating = SUT.getTitleRating(TitleDataTest.TITLE_ID)

        assertThat(titleRating, `is`(instanceOf(ResultData.Success::class.java)))
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
    fun getTitlesByGenre_correctGenrePassed() = runTest {
        val slot = slot<String>()
        successGetTitlesByGenre()

        SUT.getTitlesByGenre(TitleDataTest.GENRE_DRAMA)

        coVerify(exactly = 1) {
            titleDataSource.getTitlesByGenre(capture(slot))
        }
        assertThat(slot.captured, `is`(TitleDataTest.GENRE_DRAMA))
    }

    @Test
    fun getTitleById_correctIdPassed() = runTest {
        val slot = slot<String>()
        successGetTitleById()

        SUT.getTitleById(TitleDataTest.TITLE_ID)

        coVerify(exactly = 1) {
            titleDataSource.getTitleById(capture(slot))
        }
        assertThat(slot.captured, `is`(TitleDataTest.TITLE_ID))
    }

    //get title's rating -> check correct title's id passed to method
    @Test
    fun getTitleRating_correctTitleIdPassed() = runTest {
        val slot = slot<String>()
        successGetTitleRating()

        SUT.getTitleRating(TitleDataTest.TITLE_ID)

        coVerify(exactly = 1) {
            titleDataSource.getTitleRating(capture(slot))
        }
        assertThat(slot.captured, `is`(TitleDataTest.TITLE_ID))
    }

    //get titles by genre -> success -> store in database

    //get title's rating -> success -> store in database

    //offline cases
    //get titles with rating -> success -> titles with rating returned
    //get titles with rating -> success -> titles with rating returned

    //end offline cases

    //error cases

    //helper methods
    private suspend fun successGetTitleById() {
        coEvery { titleDataSource.getTitleById(TitleDataTest.TITLE_ID) }
            .returns(ResultData.Success<BaseNetworkData<TitleData>>(
                BaseNetworkData(TitleDataTest.TITLE_DATA)
            ))
    }

    private suspend fun successGetTitleRating() {
        coEvery { titleDataSource.getTitleRating(TitleDataTest.TITLE_ID) }
            .returns(ResultData.Success<BaseNetworkData<TitleData.Rating>>(
                BaseNetworkData(TitleDataTest.TITLE_RATING)
            ))
    }

    private suspend fun successGetTitlesByGenre() {
        coEvery { titleDataSource.getTitlesByGenre(TitleDataTest.GENRE_DRAMA) }
            .returns(ResultData.Success<BaseNetworkData<List<TitleData>>>(
                BaseNetworkData(TitleDataTest.TITLE_LIST_DATA)
            ))
    }

    private suspend fun successGetGenres() {
        coEvery { titleDataSource.getGenres() }
            .returns(ResultData.Success(BaseNetworkData(TitleDataTest.GENRES_LIST)))
    }

    //end helper methods
}