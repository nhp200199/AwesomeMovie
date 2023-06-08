package vn.com.phucars.awesomemovies.ui.titleDetail

import android.view.View
import com.bumptech.glide.Glide
import vn.com.phucars.awesomemovies.databinding.ItemActorInTitleBinding
import vn.com.phucars.awesomemovies.ui.base.adapter.BaseRecyclerViewHolder
import vn.com.phucars.awesomemovies.ui.base.adapter.RecyclerViewData

class ActorItemViewHolder(view: View) : BaseRecyclerViewHolder<ItemActorInTitleBinding>(view) {
    override fun getViewBinding(): ItemActorInTitleBinding {
        return ItemActorInTitleBinding.bind(itemView)
    }

    override fun bindData(binding: ItemActorInTitleBinding, data: RecyclerViewData) {
        if (data is Actor) {
            binding.tvActorName.text = data.name
            binding.tvCharacter.text = data.character

            Glide.with(itemView)
                .load(data.image)
                .into(binding.civAvatar)
        }
    }
}