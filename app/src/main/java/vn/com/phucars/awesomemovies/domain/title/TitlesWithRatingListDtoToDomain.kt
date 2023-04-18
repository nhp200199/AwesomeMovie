package vn.com.phucars.awesomemovies.domain.title

import vn.com.phucars.awesomemovies.mapper.Mapper
import vn.com.phucars.awesomemovies.data.title.TitleWithRatingData
import vn.com.phucars.awesomemovies.mapper.ListMapper

class TitlesWithRatingListDtoToDomain(private val mapper: Mapper<TitleWithRatingData, TitleWithRatingDomain>): ListMapper<List<TitleWithRatingData>, List<TitleWithRatingDomain>> {
    override fun map(input: List<TitleWithRatingData>): List<TitleWithRatingDomain> {
        return input.map {
            mapper.map(it)
        }
    }
}