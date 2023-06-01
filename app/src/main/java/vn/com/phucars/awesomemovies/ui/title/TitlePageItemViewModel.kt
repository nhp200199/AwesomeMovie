package vn.com.phucars.awesomemovies.ui.title

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import vn.com.phucars.awesomemovies.dispatcher.DispatcherProvider
import vn.com.phucars.awesomemovies.domain.ResultDomain
import vn.com.phucars.awesomemovies.domain.title.GetTitlesWithRatingByGenre
import vn.com.phucars.awesomemovies.domain.title.toViewState
import vn.com.phucars.awesomemovies.ui.ResultViewState
import javax.inject.Inject

@HiltViewModel
class TitlePageItemViewModel @Inject constructor(
    private val getTitlesWithRatingByGenre: GetTitlesWithRatingByGenre,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel(){
    private val currentGenre = MutableStateFlow<String>("")

    private val _loadingStateFlow = MutableStateFlow(false)
    val loadingStateFlow = _loadingStateFlow.asStateFlow()

    private val _titleWithRatingFlow = MutableSharedFlow<List<TitleWithRatingViewState>>()
    val titleWithRatingFlow = _titleWithRatingFlow.asSharedFlow()

    init {
        viewModelScope.launch(dispatcherProvider.main()) {
            currentGenre.filter { it.isNotEmpty() }.collect {
                _loadingStateFlow.value = true
                val titlesWithRatingGroupByGenre = getTitlesWithRatingByGenre(it)
                _loadingStateFlow.value = false

                if (titlesWithRatingGroupByGenre is ResultDomain.Success) {
                    _titleWithRatingFlow.emit(titlesWithRatingGroupByGenre.data.map { it.toViewState() })
                }
            }
        }
    }

    fun fetchTitlesForGenre(genre: String) {
        currentGenre.value = genre
    }
}