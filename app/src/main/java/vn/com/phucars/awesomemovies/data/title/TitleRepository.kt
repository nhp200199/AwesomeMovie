package vn.com.phucars.awesomemovies.data.title

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import vn.com.phucars.awesomemovies.domain.ResultDomain
import vn.com.phucars.awesomemovies.domain.title.TitleDomain

interface TitleRepository {
    suspend fun getGenres(): ResultDomain<List<String>>
    suspend fun getTitlePagingListByGenre(genre: String): Flow<PagingData<TitleDomain>>
    suspend fun getTitleById(id: String, info: String? = null): ResultDomain<TitleDomain>
    suspend fun searchForString(searchString: String): ResultDomain<List<TitleDomain>>
    suspend fun getTitlePagingListBySearch(searchString: String): Flow<PagingData<TitleDomain>>
}