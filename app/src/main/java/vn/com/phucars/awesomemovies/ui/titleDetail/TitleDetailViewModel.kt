package vn.com.phucars.awesomemovies.ui.titleDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import vn.com.phucars.awesomemovies.data.title.source.remote.TitleRemoteDataSource
import vn.com.phucars.awesomemovies.domain.ResultDomain
import vn.com.phucars.awesomemovies.domain.title.GetTitleById
import vn.com.phucars.awesomemovies.ui.ResultViewState
import javax.inject.Inject

@HiltViewModel
class TitleDetailViewModel @Inject constructor(private val getTitleById: GetTitleById) : ViewModel() {
    private val _titleDetailFlow = MutableStateFlow<ResultViewState<TitleDetailViewState>>(ResultViewState.Initial)
    val titleDetailFlow = _titleDetailFlow.asStateFlow()
    lateinit var id: String

    fun init(id: String) {
        this.id = id
        if (_titleDetailFlow.value == ResultViewState.Initial) {
            fetchTitleDetail(id)
        }
    }

    private fun fetchTitleDetail(id: String) {
        viewModelScope.launch {
            _titleDetailFlow.value = ResultViewState.Loading
            val resultViewState = when (val resultDomain =
                getTitleById(id)) {
                is ResultDomain.Error -> ResultViewState.Error(resultDomain.exception)
                is ResultDomain.Success -> ResultViewState.Success(
                    resultDomain.data.toDetailViewState()
                )
            }
            _titleDetailFlow.value = resultViewState
        }
    }

    fun refresh() {
        if (_titleDetailFlow.value is ResultViewState.Success
            || _titleDetailFlow.value is ResultViewState.Error) {
            fetchTitleDetail(id)
        }
    }

    //This method is used only for testing
    fun setTitleDetailState(newState: ResultViewState<TitleDetailViewState>) {
        _titleDetailFlow.value = newState
    }
}