package vn.com.phucars.awesomemovies.ui.titleSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import vn.com.phucars.awesomemovies.data.title.source.remote.SORT
import vn.com.phucars.awesomemovies.data.title.source.remote.SORT_YEAR_DECR
import vn.com.phucars.awesomemovies.data.title.source.remote.SORT_YEAR_INCR
import vn.com.phucars.awesomemovies.domain.title.SearchTitleUseCase
import vn.com.phucars.awesomemovies.ui.titleDetail.TitleDetailViewState
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TitleSearchViewModel @Inject constructor(private val searchTitleUseCase: SearchTitleUseCase) : ViewModel() {
    private val _titleSearchFlow = MutableStateFlow<String>("")
    private val _yearSortFlow = MutableStateFlow<Map<String, String?>>(emptyMap())

    private val _titleSearchDebounce = _titleSearchFlow.filter { it.isNotEmpty() }.debounce(500L)

    private val _titleSearchWithSortOptions =
        _titleSearchDebounce.combine(_yearSortFlow) { a, b ->
            Pair(a, b)
        }

    val titleSearchResultFlow = _titleSearchWithSortOptions.flatMapLatest {
        searchTitleUseCase(it.first, sortOptions = it.second).map { pagingData ->
            pagingData.map {
                it.toDetailViewState()
            }
        }
    }.cachedIn(viewModelScope)

    fun searchForTitle(searchString: String) {
        _titleSearchFlow.value = searchString
    }

    fun sortYearIncr() {
        _yearSortFlow.value = mapOf(
            SORT to SORT_YEAR_INCR
        )
    }

    fun sortYearDecr() {
        _yearSortFlow.value = mapOf(
            SORT to SORT_YEAR_DECR
        )
    }
}