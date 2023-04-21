package vn.com.phucars.awesomemovies.ui.titlesWithRating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import vn.com.phucars.awesomemovies.dispatcher.DispatcherProvider
import vn.com.phucars.awesomemovies.domain.ResultDomain
import vn.com.phucars.awesomemovies.domain.title.GetTitlesWithRatingByGenre
import vn.com.phucars.awesomemovies.ui.ResultViewState

class TitleWithRatingViewModel(
    private val getTitlesWithRatingByGenre: GetTitlesWithRatingByGenre,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val _titleWithRatingFlow: MutableStateFlow<ResultViewState<TitleWithRatingViewState>> =
        MutableStateFlow(ResultViewState.Initial)
    val titleWithRatingFlow = _titleWithRatingFlow

    fun initialize() {
        viewModelScope.launch(dispatcherProvider.main()) {
            _titleWithRatingFlow.value = ResultViewState.Loading

            val titlesWithRatingByGenre =
                getTitlesWithRatingByGenre.getTitlesWithRatingByGenre("hello")

            if (titlesWithRatingByGenre is ResultDomain.Success) {
                _titleWithRatingFlow.value = ResultViewState.Success(TitleWithRatingViewState())
            } else {
                _titleWithRatingFlow.value =
                    ResultViewState.Error((titlesWithRatingByGenre as ResultDomain.Error).exception)
            }
        }
    }
}