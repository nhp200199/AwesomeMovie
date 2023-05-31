package vn.com.phucars.awesomemovies.ui.base.adapter

import androidx.recyclerview.widget.DiffUtil

class BaseDiffCallback internal constructor(
    private val oldData: List<RecyclerViewData>,
    private val newData: List<RecyclerViewData>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].areItemsTheSame(newData[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].areContentsTheSame(newData[newItemPosition])
    }
}