package vn.com.phucars.awesomemovies.ui.title

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import vn.com.phucars.awesomemovies.dispatcher.DispatcherProvider
import vn.com.phucars.awesomemovies.domain.ResultDomain
import vn.com.phucars.awesomemovies.domain.title.GetTitleWithRatingListGroupByGenre
import vn.com.phucars.awesomemovies.ui.ResultViewState
import javax.inject.Inject

@HiltViewModel
class TitleWithRatingViewModel @Inject constructor(
    private val getTitleWithRatingListGroupByGenre: GetTitleWithRatingListGroupByGenre,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val _titleWithRatingFlow: MutableStateFlow<ResultViewState<TitleWithRatingViewState>> =
        MutableStateFlow(ResultViewState.Initial)
    val titleWithRatingFlow = _titleWithRatingFlow.asStateFlow()

    fun initialize() {
        _titleWithRatingFlow.value = ResultViewState.Loading
        viewModelScope.launch(dispatcherProvider.main()) {
            val titlesWithRatingGroupByGenre =
                getTitleWithRatingListGroupByGenre.getTitleWithRatingListGroupByGenre()

            if (titlesWithRatingGroupByGenre is ResultDomain.Success) {
                _titleWithRatingFlow.value = ResultViewState.Success(TitleWithRatingViewState())
            } else {
                _titleWithRatingFlow.value =
                    ResultViewState.Error((titlesWithRatingGroupByGenre as ResultDomain.Error).exception)
            }
        }
    }
}