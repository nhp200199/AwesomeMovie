package vn.com.phucars.awesomemovies.ui.genre

import vn.com.phucars.awesomemovies.R
import vn.com.phucars.awesomemovies.ui.base.adapter.RecyclerViewData

data class GenreViewState(
    val genre: String,
    val selected: Boolean
) : RecyclerViewData {
    override fun getViewType(): Int {
        return R.layout.item_genre
    }

    override fun areItemsTheSame(other: RecyclerViewData): Boolean {
        return genre == (other as GenreViewState).genre
    }

    override fun areContentsTheSame(other: RecyclerViewData): Boolean {
        return selected == (other as GenreViewState).selected
    }
}
