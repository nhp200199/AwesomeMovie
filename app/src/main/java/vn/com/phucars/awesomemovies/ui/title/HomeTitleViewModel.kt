package vn.com.phucars.awesomemovies.ui.title

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.domain.genre.GetGenreListUseCase
import vn.com.phucars.awesomemovies.ui.ResultViewState
import vn.com.phucars.awesomemovies.ui.genre.GenreViewState
import javax.inject.Inject

@HiltViewModel
class HomeTitleViewModel @Inject constructor(
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
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ResultViewState.Initial)

    init {
        fetchGenres()
    }

    private fun fetchGenres() {
        _genreViewStateFlow.value = ResultViewState.Loading
        viewModelScope.launch {
            val resultViewState = when (val genreListResult = getGenreListUseCase()) {
                is ResultData.Error -> ResultViewState.Error(genreListResult.exception)
                is ResultData.Success -> {
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

    fun refresh() {
        if (_genreViewStateFlow.value is ResultViewState.Success
            || _genreViewStateFlow.value is ResultViewState.Error) {
            fetchGenres()
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

    fun getSelectedGenre(): Int {
        var selectedGenreIdx  = 0;
        for ((index, genre) in genreList.withIndex()) {
            if (genre == selectedGenre.value) {
                selectedGenreIdx = index
                break
            }
        }
        return selectedGenreIdx
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("TitleHome", "onCleared")
    }
}