package vn.com.phucars.awesomemovies.ui.title

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import vn.com.phucars.awesomemovies.domain.ResultDomain
import vn.com.phucars.awesomemovies.domain.genre.GetGenreListUseCase
import vn.com.phucars.awesomemovies.ui.ResultViewState
import vn.com.phucars.awesomemovies.ui.genre.GenreViewState
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getGenreListUseCase: GetGenreListUseCase
) : ViewModel() {
    private val selectedGenre = MutableStateFlow<String?>(null)
    private var genreList = emptyList<String>()

    private val _genreViewStateFlow =
        MutableStateFlow<ResultViewState<List<GenreViewState>>>(ResultViewState.Initial)
    val genreViewStateFlow = combine(_genreViewStateFlow, selectedGenre) { genreListResult, selectedG ->
        when(genreListResult) {
            is ResultViewState.Error,
            ResultViewState.Initial,
            ResultViewState.Loading -> genreListResult
            is ResultViewState.Success -> {
                if (selectedG.isNullOrEmpty()) {
                    selectedGenre.value = genreListResult.data[0].genre
                    genreListResult
                } else {
                    val genreViewStates = genreListResult.data.map { genre ->
                        GenreViewState(genre.genre, genre.genre == selectedGenre.value)
                    }
                    ResultViewState.Success(genreViewStates)
                }
            }
        }
    }

    init {
        _genreViewStateFlow.value = ResultViewState.Loading
        viewModelScope.launch {
            val resultViewState = when (val genreListResult = getGenreListUseCase()) {
                is ResultDomain.Error -> ResultViewState.Error(genreListResult.exception)
                is ResultDomain.Success -> {
                    genreList = genreListResult.data
                    val genreViewStates = genreListResult.data.map { genre ->
                        GenreViewState(genre, false)
                    }
                    ResultViewState.Success(genreViewStates)
                }
            }
            _genreViewStateFlow.value = resultViewState
        }
    }

    fun selectGenre(position: Int) {
        if (genreList.isNotEmpty() && genreList.size > position) {
            selectGenre(genreList[position])
        }
    }

    fun selectGenre(newGenre: String) {
        selectedGenre.value = newGenre
    }
}