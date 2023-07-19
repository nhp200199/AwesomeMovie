package vn.com.phucars.awesomemovies.ui.genre

import android.graphics.drawable.ShapeDrawable
import android.view.View
import vn.com.phucars.awesomemovies.R
import vn.com.phucars.awesomemovies.databinding.ItemGenreBinding
import vn.com.phucars.awesomemovies.ui.base.adapter.BaseRecyclerViewHolder
import vn.com.phucars.awesomemovies.ui.base.adapter.RecyclerViewData

class GenreItemViewHolder(itemView: View) : BaseRecyclerViewHolder<ItemGenreBinding>(itemView) {
    private val context = itemView.context

    override fun getViewBinding(): ItemGenreBinding {
        return ItemGenreBinding.bind(itemView)
    }

    override fun bindData(binding: ItemGenreBinding, data: RecyclerViewData) {
        if (data is GenreViewState) {
            binding.tvGenre.text = data.genre

            if (data.selected) {
                binding.tvGenre.setTextColor(context.resources.getColor(R.color.teal_400))
                binding.tvGenre.background = context.getDrawable(R.drawable.round_white_bg)
            } else {
                binding.tvGenre.setTextColor(context.resources.getColor(R.color.white))
                binding.tvGenre.background = context.getDrawable(R.drawable.round_black_bg)
            }
        }
    }
}