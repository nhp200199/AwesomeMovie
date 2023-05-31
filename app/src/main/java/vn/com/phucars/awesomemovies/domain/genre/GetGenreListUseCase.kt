package vn.com.phucars.awesomemovies.domain.genre

import vn.com.phucars.awesomemovies.data.title.TitleRepository
import vn.com.phucars.awesomemovies.domain.ResultDomain
import javax.inject.Inject

class GetGenreListUseCase @Inject constructor(private val repository: TitleRepository) {
    suspend operator fun invoke(): ResultDomain<List<String>> {
        return repository.getGenres()
    }
}