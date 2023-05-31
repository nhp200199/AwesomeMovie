package vn.com.phucars.awesomemovies.domain.genre

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
import vn.com.phucars.awesomemovies.testdata.GenreDataTest

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class GetGenreListUseCaseTest {
    private lateinit var repository: TitleRepository
    private lateinit var getGenreListUseCase: GetGenreListUseCase

    @Before
    fun setup() {
        repository = mockk<TitleRepository>()
        getGenreListUseCase = GetGenreListUseCase(repository)
    }

    @Test
    fun getGenreList_success_genreListReturned() = runTest {
        coEvery {
            repository.getGenres()
        }.returns(ResultDomain.Success(GenreDataTest.GENRES_LIST.filterNotNull()))

        val result: ResultDomain<List<String>> = getGenreListUseCase()

        assertThat(result, `is`(instanceOf(ResultDomain.Success::class.java)))
    }

    @Test
    fun getGenreList_generalError_generalErrorReturned() = runTest {
        coEvery {
            repository.getGenres()
        }.returns(ResultDomain.Error(Exception()))

        val result: ResultDomain<List<String>> = getGenreListUseCase()

        assertThat(result, `is`(instanceOf(ResultDomain.Error::class.java)))
    }
}