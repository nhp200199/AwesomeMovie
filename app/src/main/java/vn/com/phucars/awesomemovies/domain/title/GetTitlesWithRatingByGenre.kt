package vn.com.phucars.awesomemovies.domain.title

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import vn.com.phucars.awesomemovies.data.title.TitleRepository
import vn.com.phucars.awesomemovies.domain.ResultDomain
import javax.inject.Inject

class GetTitlesWithRatingByGenre @Inject constructor(private val repository: TitleRepository) {
    suspend operator fun invoke(genre: String): Flow<PagingData<TitleWithRatingDomain>> {
        return repository.getTitleWithRatingPagingListByGenre(genre)
    }
}