package vn.com.phucars.awesomemovies.ui.base.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BaseRecyclerAdapter<RECYCLER_VIEW_DATA : RecyclerViewData, VIEW_HOLDER_BINDING : ViewBinding>(
    private var data: List<RECYCLER_VIEW_DATA> = emptyList()
) : RecyclerView.Adapter<BaseRecyclerViewHolder<VIEW_HOLDER_BINDING>>() {
    private var onItemClickedListener: ((Int, View) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerViewHolder<VIEW_HOLDER_BINDING> {
        Log.d("BaseRecyclerAdapter", "onCreateViewHolder")
        val createViewHolder =
            ViewHolderFactory.createViewHolder<VIEW_HOLDER_BINDING>(viewType, parent)
        createViewHolder.setOnItemClickedListener(onItemClickedListener)
        return createViewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(
        holder: BaseRecyclerViewHolder<VIEW_HOLDER_BINDING>,
        position: Int
    ) {
        holder.bindData(holder.getViewBinding(), data[position])
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].getViewType()
    }

    fun setOnItemClickedListener(listener: ((Int, View) -> Unit)?) {
        this.onItemClickedListener = listener
    }

    fun update(newData: List<RECYCLER_VIEW_DATA>) {
        val diffCallback = BaseDiffCallback(data, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        data = newData
//        notifyDataSetChanged()
    }
}