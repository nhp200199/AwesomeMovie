package vn.com.phucars.awesomemovies.domain.title

import vn.com.phucars.awesomemovies.data.title.NewTitleRemoteData
import vn.com.phucars.awesomemovies.data.title.PrincipalCast
import vn.com.phucars.awesomemovies.ui.title.TitleWithRatingViewState
import vn.com.phucars.awesomemovies.ui.titleDetail.Actor
import vn.com.phucars.awesomemovies.ui.titleDetail.TitleDetailViewState

data class TitleWithRatingDomain(
    val id: String,
    val imageUrl: String,
    val titleText: String,
    val releaseDate: String,
    val averageRating: Float,
    val numVotes: Int,
    val description: String? = null,
    val trailer: String? = null,
    val genres: List<String>? = null,
    val castings: List<PrincipalCast>? = null,
    val duration: Int? = null
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
        val durationTime = this.duration?.let {
            val hour = if (it / 3600 != 0) {
                "${it / 3600}h"
            } else ""

            val minute = if (it % 3600 != 0) {
                "${it % 3600}m"
            } else ""


            return@let "$hour$minute"
        } ?: "---"

        return TitleDetailViewState(
            this.titleText,
            this.genres ?: listOf(),
            this.imageUrl,
            this.releaseDate,
            this.description ?: "No information",
            this.castings?.get(0)?.credits?.map {
                Actor(
                    it.name.id,
                    it.name.primaryImage?.url ?: "",
                    it.name.nameText.text,
                    it.characters?.get(0)?.name ?: "---"
                )
            } ?: listOf(),
            this.averageRating.toDouble(),
            this.numVotes,
            durationTime
        )
    }
}

