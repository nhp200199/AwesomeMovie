package vn.com.phucars.awesomemovies.domain.title

import vn.com.phucars.awesomemovies.data.title.TitleRepository
import vn.com.phucars.awesomemovies.domain.ResultDomain

class GetTitlesWithRatingByGenre(private val repository: TitleRepository) {
    suspend fun getTitlesWithRatingByGenre(genre: String): ResultDomain<List<TitleWithRatingDomain>> {
        return repository.getTitleWithRatingListByGenre(genre)
    }
}