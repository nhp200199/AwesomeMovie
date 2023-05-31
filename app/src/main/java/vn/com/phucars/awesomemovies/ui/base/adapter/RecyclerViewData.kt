package vn.com.phucars.awesomemovies.ui.base.adapter

import androidx.annotation.LayoutRes

interface RecyclerViewData {
    @LayoutRes
    fun getViewType(): Int
    fun areItemsTheSame(other: RecyclerViewData): Boolean
    fun areContentsTheSame(other: RecyclerViewData): Boolean
}