package vn.com.phucars.awesomemovies.ui.title

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.mockito.Mockito.*
import org.hamcrest.MatcherAssert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.hamcrest.CoreMatchers.*
import org.junit.Rule
import org.junit.Test
import vn.com.phucars.awesomemovies.CoroutineTestRule
import vn.com.phucars.awesomemovies.domain.genre.GetGenreListUseCase

@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {
    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    lateinit var SUT: MainActivityViewModel
    lateinit var getGenreListUseCase: GetGenreListUseCase

    @Before
    fun setup() {
        getGenreListUseCase = mockk<GetGenreListUseCase>()
        SUT = MainActivityViewModel(getGenreListUseCase)
    }

    //initialize - begin get genre list - emit loading state
    @Test
    fun initialize_beginGetGenreList_emitLoadingState() = runTest {
        coEvery { getGenreListUseCase() }
    }
    //initialize - get genre list success - emit list of genres view state with first item selected
    //initialize - get genre list success - cached first item as selected
    //initialize - get genre list fail general error - emit fail state

    //selectGenre - emit selected genre

    //selected genre changed - emit loading state
    //selected
}