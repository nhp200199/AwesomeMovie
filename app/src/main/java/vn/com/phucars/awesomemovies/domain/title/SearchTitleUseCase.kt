package vn.com.phucars.awesomemovies.domain.title

import vn.com.phucars.awesomemovies.data.title.TitleRepository
import vn.com.phucars.awesomemovies.domain.ResultDomain
import javax.inject.Inject

class SearchTitleUseCase @Inject constructor(private val titleRepository: TitleRepository) {
    suspend operator fun invoke(searchString: String): ResultDomain<List<TitleWithRatingDomain>> {
        return titleRepository.searchForString(searchString)
    }
}