package vn.com.phucars.awesomemovies.domain.title.mapper

import vn.com.phucars.awesomemovies.data.title.TitleData
import vn.com.phucars.awesomemovies.domain.title.TitleDomain
import vn.com.phucars.awesomemovies.mapper.Mapper

class TitleDataRemoteDtoToDomain : Mapper<TitleData, TitleDomain> {
    override fun map(input: TitleData): TitleDomain {
        val genres = if (input.genres != null) {
            input.genres.genres?.mapNotNull {
                it.text
            } ?: emptyList()
        } else emptyList()

        return TitleDomain(
            input.id,
            input.primaryImage?.url ?: TitleDomain.UNKNOWN_TITLE_IMAGE,
            input.titleText?.text ?: TitleDomain.UNKNOWN_TITLE_TEXT,
            if (input.releaseDate != null) "${input.releaseDate.day}-${input.releaseDate.month}-${input.releaseDate.year}" else TitleDomain.UNKNOWN_TITLE_RELEASE_DATE,
            input.ratingsSummary?.aggregateRating ?: TitleDomain.UNKNOWN_TITLE_RATING,
            input.ratingsSummary?.voteCount ?: TitleDomain.UNKNOWN_TITLE_NUM_VOTE,
            input.plot?.plotText?.plainText ?: TitleDomain.UNKNOWN_TITLE_DESCRIPTION,
            input.plot?.trailer ?: TitleDomain.UNKNOWN_TITLE_TRAILER_URL,
            genres,
            input.principalCast ?: emptyList(),
            input.runtime?.seconds ?: TitleDomain.UNKNOWN_TITLE_DURATION
        )
    }
}