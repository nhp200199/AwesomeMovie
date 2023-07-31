package vn.com.phucars.awesomemovies.ui.titleSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import vn.com.phucars.awesomemovies.domain.ResultDomain
import vn.com.phucars.awesomemovies.domain.title.SearchTitleUseCase
import vn.com.phucars.awesomemovies.ui.ResultViewState
import vn.com.phucars.awesomemovies.ui.title.TitleWithRatingViewState
import vn.com.phucars.awesomemovies.ui.titleDetail.TitleDetailViewState
import javax.inject.Inject

@HiltViewModel
class TitleSearchViewModel @Inject constructor(private val searchTitleUseCase: SearchTitleUseCase) : ViewModel() {
    private val _titleSearchResultFlow = MutableStateFlow<ResultViewState<List<TitleWithRatingViewState>>>(ResultViewState.Initial)
    val titleSearchResultFlow = _titleSearchResultFlow.asStateFlow()

    fun searchForTitle(searchString: String) {
        _titleSearchResultFlow.value = ResultViewState.Loading

        viewModelScope.launch {
            val searchTitleResult = searchTitleUseCase(searchString)
            when(searchTitleResult) {
                is ResultDomain.Error -> _titleSearchResultFlow.value = ResultViewState.Error(searchTitleResult.exception)
                is ResultDomain.Success -> _titleSearchResultFlow.value = ResultViewState.Success(searchTitleResult.data.map {
                    it.toViewState()
                })
            }
        }
    }
}