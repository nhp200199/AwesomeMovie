package vn.com.phucars.awesomemovies.ui.titleSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import vn.com.phucars.awesomemovies.domain.title.SearchTitleUseCase
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TitleSearchViewModel @Inject constructor(private val searchTitleUseCase: SearchTitleUseCase) : ViewModel() {
    private val _titleSearchFlow = MutableStateFlow<String>("")

    val titleSearchResultFlow = _titleSearchFlow.filter { it.isNotEmpty() }.flatMapLatest {
        searchTitleUseCase(it).map { pagingData ->
            pagingData.map {
                it.toDetailViewState()
            }
        }
    }.cachedIn(viewModelScope)

    fun searchForTitle(searchString: String) {
        _titleSearchFlow.value = searchString
    }
}