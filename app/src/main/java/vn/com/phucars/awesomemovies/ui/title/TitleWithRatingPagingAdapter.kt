package vn.com.phucars.awesomemovies.ui.title

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import vn.com.phucars.awesomemovies.R
import vn.com.phucars.awesomemovies.databinding.ItemTitleBinding
import vn.com.phucars.awesomemovies.ui.base.adapter.ViewHolderFactory

class TitleWithRatingPagingAdapter(diffCallback: DiffUtil.ItemCallback<TitleWithRatingViewState>) :
    PagingDataAdapter<TitleWithRatingViewState, TitleItemViewHolder>(diffCallback) {
    override fun onBindViewHolder(holder: TitleItemViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bindData(holder.getViewBinding(), it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleItemViewHolder {
        val createViewHolder =
            ViewHolderFactory.createViewHolder<ItemTitleBinding>(viewType, parent)
        return createViewHolder as TitleItemViewHolder
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.getViewType() ?: 0
    }
}