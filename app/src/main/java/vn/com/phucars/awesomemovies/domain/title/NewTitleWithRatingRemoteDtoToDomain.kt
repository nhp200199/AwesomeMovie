package vn.com.phucars.awesomemovies.domain.title

import vn.com.phucars.awesomemovies.data.title.NewTitleRemoteData
import vn.com.phucars.awesomemovies.data.title.TitleData
import vn.com.phucars.awesomemovies.mapper.Mapper

class NewTitleWithRatingRemoteDtoToDomain : Mapper<NewTitleRemoteData, TitleWithRatingDomain> {
    override fun map(input: NewTitleRemoteData): TitleWithRatingDomain {
        return TitleWithRatingDomain(
            input.id,
            input.primaryImage?.url ?: "",
            input.titleText.text,
            if (input.releaseDate != null) "${input.releaseDate.day}-${input.releaseDate.month}-${input.releaseDate.year}" else "---",
            input.ratingsSummary?.averageRating ?: TitleData.Rating.DEFAULT_VALUE.averageRating,
            input.ratingsSummary?.numVotes ?: TitleData.Rating.DEFAULT_VALUE.numVotes,
            input.plot?.plotText?.plainText,
            input.plot?.trailer,
            input.genres.genres.map { it.text },
            input.principalCast
        )
    }
}