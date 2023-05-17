package vn.com.phucars.awesomemovies.ui.title

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import vn.com.phucars.awesomemovies.dispatcher.DispatcherProvider
import vn.com.phucars.awesomemovies.domain.ResultDomain
import vn.com.phucars.awesomemovies.domain.title.GetTitleWithRatingListGroupByGenre
import vn.com.phucars.awesomemovies.domain.title.GetTitlesWithRatingByGenre
import vn.com.phucars.awesomemovies.ui.ResultViewState

class TitleWithRatingViewModel(
    private val getTitleWithRatingListGroupByGenre: GetTitleWithRatingListGroupByGenre,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val _titleWithRatingFlow: MutableStateFlow<ResultViewState<TitleWithRatingViewState>> =
        MutableStateFlow(ResultViewState.Initial)
    val titleWithRatingFlow = _titleWithRatingFlow.asStateFlow()

    fun initialize() {
        _titleWithRatingFlow.value = ResultViewState.Loading
        viewModelScope.launch(dispatcherProvider.main()) {
            val titlesWithRatingByGenre =
                getTitleWithRatingListGroupByGenre.getTitleWithRatingListGroupByGenre()

            if (titlesWithRatingByGenre is ResultDomain.Success) {
                _titleWithRatingFlow.value = ResultViewState.Success(TitleWithRatingViewState())
            } else {
                _titleWithRatingFlow.value =
                    ResultViewState.Error((titlesWithRatingByGenre as ResultDomain.Error).exception)
            }
        }
    }
}