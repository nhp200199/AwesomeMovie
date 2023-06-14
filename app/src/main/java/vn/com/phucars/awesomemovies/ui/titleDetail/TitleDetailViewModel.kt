package vn.com.phucars.awesomemovies.ui.titleDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import vn.com.phucars.awesomemovies.domain.ResultDomain
import vn.com.phucars.awesomemovies.domain.title.GetTitleById
import vn.com.phucars.awesomemovies.ui.ResultViewState
import javax.inject.Inject

@HiltViewModel
class TitleDetailViewModel @Inject constructor(private val getTitleById: GetTitleById) : ViewModel() {
    private val _titleDetailFlow = MutableStateFlow<ResultViewState<TitleDetailViewState>>(ResultViewState.Initial)
    val titleDetailFlow = _titleDetailFlow.asStateFlow()

    fun init(id: String, info: String? = null) {
        if (_titleDetailFlow.value == ResultViewState.Initial) {
            viewModelScope.launch {
                _titleDetailFlow.value = ResultViewState.Loading
                val resultViewState = when (val resultDomain = getTitleById.invoke(id, info)) {
                    is ResultDomain.Error -> TODO()
                    is ResultDomain.Success -> ResultViewState.Success(
                        resultDomain.data.toDetailViewState()
                    )
                }
                _titleDetailFlow.value = resultViewState
            }
        }
    }
}