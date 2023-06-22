package vn.com.phucars.awesomemovies.ui.title

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.*
import vn.com.phucars.awesomemovies.R

class TitleLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadStateViewHolder>() {
    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_load_state, parent, false)
        return LoadStateViewHolder(view, retry)
    }
}