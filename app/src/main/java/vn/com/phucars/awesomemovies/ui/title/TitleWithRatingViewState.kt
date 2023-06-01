package vn.com.phucars.awesomemovies.ui.title

import vn.com.phucars.awesomemovies.R
import vn.com.phucars.awesomemovies.ui.base.adapter.RecyclerViewData

data class TitleWithRatingViewState(
    val id: String,
    val imageUrl: String,
    val titleText: String,
    val releaseDate: String,
    val averageRating: Float,
    val numVotes: Int
) : RecyclerViewData {
    override fun getViewType(): Int {
        return R.layout.item_title
    }

    override fun areItemsTheSame(other: RecyclerViewData): Boolean {
        return this.id == (other as? TitleWithRatingViewState)?.id
    }

    override fun areContentsTheSame(other: RecyclerViewData): Boolean {
        return areItemsTheSame(other)
    }
}