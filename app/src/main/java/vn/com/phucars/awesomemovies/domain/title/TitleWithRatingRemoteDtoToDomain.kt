package vn.com.phucars.awesomemovies.domain.title

import vn.com.phucars.awesomemovies.mapper.Mapper
import vn.com.phucars.awesomemovies.data.title.TitleWithRatingRemoteData

class TitleWithRatingRemoteDtoToDomain: Mapper<TitleWithRatingRemoteData, TitleWithRatingDomain> {
    override fun map(input: TitleWithRatingRemoteData): TitleWithRatingDomain {
        return TitleWithRatingDomain(
            input.id,
            input.primaryImage.url,
            input.titleText.text,
            "${input.releaseDate.day}-${input.releaseDate.month}-${input.releaseDate.year}",
            input.rating.averageRating,
            input.rating.numVotes
        )
    }
}