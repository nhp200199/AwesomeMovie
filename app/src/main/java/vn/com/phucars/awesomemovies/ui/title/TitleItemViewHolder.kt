package vn.com.phucars.awesomemovies.ui.title

import android.view.View
import com.bumptech.glide.Glide
import vn.com.phucars.awesomemovies.R
import vn.com.phucars.awesomemovies.databinding.ItemTitleBinding
import vn.com.phucars.awesomemovies.ui.base.adapter.BaseRecyclerViewHolder
import vn.com.phucars.awesomemovies.ui.base.adapter.RecyclerViewData
import vn.com.phucars.awesomemovies.ui.titleDetail.TitleDetailViewState

class TitleItemViewHolder(view: View) : BaseRecyclerViewHolder<ItemTitleBinding>(view) {
    override fun getViewBinding(): ItemTitleBinding {
        return ItemTitleBinding.bind(itemView)
    }

    override fun bindData(binding: ItemTitleBinding, data: RecyclerViewData) {
        if (data is TitleWithRatingViewState) {
            binding.tvTitle.text = data.titleText
            binding.tvReleaseDate.text = data.releaseDate
            binding.tvRating.text = data.averageRating.toString()

            Glide.with(binding.ivImage.context)
                .load(data.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(binding.ivImage)
        } else if (data is TitleDetailViewState) {
            binding.tvTitle.text = data.title
            binding.tvReleaseDate.text = data.releaseYear
            binding.tvRating.text = data.rating.toString()

            Glide.with(binding.ivImage.context)
                .load(data.posterUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(binding.ivImage)
        }
    }

}
