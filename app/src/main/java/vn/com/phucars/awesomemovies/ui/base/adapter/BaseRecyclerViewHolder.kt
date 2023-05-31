package vn.com.phucars.awesomemovies.ui.base.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding

abstract class BaseRecyclerViewHolder<T: ViewBinding>(itemView: View) : ViewHolder(itemView) {
    abstract fun getViewBinding(): T
    abstract fun bindData(binding: T, data: RecyclerViewData)
    fun setOnItemClickedListener(listener: ((Int, View) -> Unit)?) {
        itemView.setOnClickListener {
            listener?.invoke(adapterPosition, itemView)
        }
    }
}