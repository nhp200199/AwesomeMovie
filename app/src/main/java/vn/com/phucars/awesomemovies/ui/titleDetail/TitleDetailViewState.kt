package vn.com.phucars.awesomemovies.ui.titleDetail

import vn.com.phucars.awesomemovies.R
import vn.com.phucars.awesomemovies.ui.base.adapter.RecyclerViewData

data class TitleDetailViewState(
    val id: String,
    val title: String,
    val genres: List<String>,
    val posterUrl: String,
    val releaseYear: String,
    val description: String,
    val castings: List<Actor>,
    val rating: Double,
    val voteCount: Int,
    val duration: String,
) {
}

data class Actor(
    val id: String,
    val image: String,
    val name: String,
    val character: String
) : RecyclerViewData {
    override fun getViewType(): Int {
        return R.layout.item_actor_in_title
    }

    override fun areItemsTheSame(other: RecyclerViewData): Boolean {
        return this.id == (other as Actor).id
    }

    override fun areContentsTheSame(other: RecyclerViewData): Boolean {
        return areItemsTheSame(other)
    }
}