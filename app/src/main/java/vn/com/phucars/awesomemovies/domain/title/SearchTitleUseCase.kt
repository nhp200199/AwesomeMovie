package vn.com.phucars.awesomemovies.domain.title

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import vn.com.phucars.awesomemovies.data.title.TitleRepository
import javax.inject.Inject

class SearchTitleUseCase @Inject constructor(private val titleRepository: TitleRepository) {
    suspend operator fun invoke(searchString: String, sortOptions: Map<String, String?> = emptyMap()): Flow<PagingData<TitleDomain>> {
        return titleRepository.getTitlePagingListBySearch(searchString, sortOptions = sortOptions)
    }
}