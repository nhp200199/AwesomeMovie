package vn.com.phucars.awesomemovies.domain.title

import vn.com.phucars.awesomemovies.data.title.PrincipalCast
import vn.com.phucars.awesomemovies.ui.title.TitleWithRatingViewState
import vn.com.phucars.awesomemovies.ui.titleDetail.Actor
import vn.com.phucars.awesomemovies.ui.titleDetail.TitleDetailViewState

data class TitleDomain(
    val id: String,
    val imageUrl: String,
    val titleText: String,
    val releaseDate: String,
    val averageRating: Float,
    val numVotes: Int,
    val description: String,
    val trailer: String,
    val genres: List<String>,
    val castings: List<PrincipalCast>,
    val duration: Int
) {

    fun toViewState() = TitleWithRatingViewState(
        this.id,
        this.imageUrl,
        this.titleText,
        this.releaseDate,
        this.averageRating,
        this.numVotes
    )

    fun toDetailViewState(): TitleDetailViewState {
        val durationTime = if (duration != null) {
            val hour = if (duration / 3600 != 0) {
                "${duration / 3600}h"
            } else ""

            val minute = if (duration % 3600 != 0) {
                "${duration % 3600}m"
            } else ""

            "$hour$minute"
        } else "---"

        val credits = if (castings.isNotEmpty()) {
            castings[0].credits?.map {
                Actor(
                    it.name?.id ?: "",
                    it.name?.primaryImage?.url ?: "",
                    it.name?.nameText?.text ?: "",
                    it.characters?.get(0)?.name ?: "---"
                )
            } ?: emptyList()
        } else {
            emptyList()
        }

        return TitleDetailViewState(
            this.id,
            this.titleText,
            this.genres,
            this.imageUrl,
            this.releaseDate,
            this.description,
            credits,
            this.averageRating,
            this.numVotes,
            durationTime
        )
    }

    companion object {
        val UNKNOWN_TITLE_IMAGE = "---"
        val UNKNOWN_TITLE_TEXT = "---"
        val UNKNOWN_TITLE_RELEASE_DATE = "---"
        val UNKNOWN_TITLE_RATING = -1f
        val UNKNOWN_TITLE_NUM_VOTE = -1
        val UNKNOWN_TITLE_DESCRIPTION = "---"
        val UNKNOWN_TITLE_TRAILER_URL = ""
        val UNKNOWN_TITLE_DURATION = -1
    }
}

