package vn.com.phucars.awesomemovies.data.title

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.domain.title.TitleDomain

interface TitleRepository {
    suspend fun getGenres(): ResultData<List<String>>
    suspend fun getTitlePagingListByGenre(genre: String): Flow<PagingData<TitleDomain>>
    suspend fun getTitleById(id: String, info: String? = null): ResultData<TitleDomain>
    suspend fun searchForString(searchString: String, searchOptions: Map<String, String?> = emptyMap()): ResultData<List<TitleDomain>>
    suspend fun getTitlePagingListBySearch(searchString: String, sortOptions: Map<String, String?> = emptyMap()): Flow<PagingData<TitleDomain>>
}