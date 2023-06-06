package vn.com.phucars.awesomemovies.ui.title

import android.view.View
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import vn.com.phucars.awesomemovies.databinding.ItemLoadStateBinding

class LoadStateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemLoadStateBinding.bind(itemView)

    fun bind(loadState: LoadState) {
        binding.pbTitle.isVisible = loadState is LoadState.Loading
        binding.llRetryContainer.isVisible = loadState is LoadState.Error
    }
}