package vn.com.phucars.awesomemovies.domain.title

import kotlinx.coroutines.*
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.title.TitleData
import vn.com.phucars.awesomemovies.data.title.TitleRepository
import vn.com.phucars.awesomemovies.data.title.TitleWithRatingData
import vn.com.phucars.awesomemovies.domain.ResultDomain
import vn.com.phucars.awesomemovies.mapper.ListMapper

class GetTitlesWithRatingByGenre(private val repository: TitleRepository, private val mapper: ListMapper<List<TitleWithRatingData>, List<TitleWithRatingDomain>>) {
    suspend fun getTitlesWithRatingByGenre(genre: String): ResultDomain<List<TitleWithRatingDomain>> {
        val titlesWithRatingByGenre = repository.getTitlesWithRatingByGenre(genre)
        return if (titlesWithRatingByGenre is ResultData.Success) {
            ResultDomain.Success<List<TitleWithRatingDomain>>(mapper.map(titlesWithRatingByGenre.data))
        } else throw Exception()
    }
}