package vn.com.phucars.awesomemovies.domain.title.mapper

import vn.com.phucars.awesomemovies.data.title.TitleData
import vn.com.phucars.awesomemovies.mapper.Mapper
import vn.com.phucars.awesomemovies.data.title.TitleWithRatingRemoteData
import vn.com.phucars.awesomemovies.domain.title.TitleWithRatingDomain

class TitleWithRatingRemoteDtoToDomain: Mapper<TitleWithRatingRemoteData, TitleWithRatingDomain> {
    override fun map(input: TitleWithRatingRemoteData): TitleWithRatingDomain {
        return TitleWithRatingDomain(
            input.id,
            input.primaryImage?.url ?: "",
            input.titleText.text,
            if (input.releaseDate != null) "${input.releaseDate.day}-${input.releaseDate.month}-${input.releaseDate.year}" else "---",
            input.rating?.averageRating ?: TitleData.Rating.DEFAULT_VALUE.averageRating,
            input.rating?.numVotes ?: TitleData.Rating.DEFAULT_VALUE.numVotes
        )
    }
}