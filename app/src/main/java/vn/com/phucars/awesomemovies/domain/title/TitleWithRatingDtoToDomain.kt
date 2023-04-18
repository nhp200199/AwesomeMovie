package vn.com.phucars.awesomemovies.domain.title

import vn.com.phucars.awesomemovies.mapper.Mapper
import vn.com.phucars.awesomemovies.data.title.TitleWithRatingData

class TitleWithRatingDtoToDomain: Mapper<TitleWithRatingData, TitleWithRatingDomain> {
    override fun map(input: TitleWithRatingData): TitleWithRatingDomain {
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