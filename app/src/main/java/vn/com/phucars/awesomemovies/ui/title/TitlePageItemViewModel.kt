package vn.com.phucars.awesomemovies.ui.title

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val titleWithRatingFlow: Flow<PagingData<TitleWithRatingViewState>> =
        currentGenre.flatMapLatest {
            getTitlesWithRatingByGenre(it).map { pagingData ->
                pagingData.map {
                    it.toViewState()
                }
            }
        }.cachedIn(viewModelScope)



    fun fetchTitlesForGenre(genre: String) {
        currentGenre.value = genre
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("TitlePageItem", "onCleared")
    }
}