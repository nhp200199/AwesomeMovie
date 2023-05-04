package vn.com.phucars.awesomemovies.domain.title

import vn.com.phucars.awesomemovies.mapper.Mapper
import vn.com.phucars.awesomemovies.data.title.TitleWithRatingRemoteData
import vn.com.phucars.awesomemovies.mapper.ListMapper

class TitleWithRatingListRemoteDtoToDomain(private val mapper: Mapper<TitleWithRatingRemoteData, TitleWithRatingDomain>): ListMapper<TitleWithRatingRemoteData, TitleWithRatingDomain> {
    override fun map(input: List<TitleWithRatingRemoteData>): List<TitleWithRatingDomain> {
        return input.map {
            mapper.map(it)
        }
    }
}