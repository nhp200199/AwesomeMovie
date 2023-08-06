package vn.com.phucars.awesomemovies.domain.genre

import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.title.TitleRepository
import javax.inject.Inject

class GetGenreListUseCase @Inject constructor(private val repository: TitleRepository) {
    suspend operator fun invoke(): ResultData<List<String>> {
        return repository.getGenres()
    }
}