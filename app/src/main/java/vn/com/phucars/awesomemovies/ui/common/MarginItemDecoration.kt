package vn.com.phucars.awesomemovies.ui.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager

class MarginItemDecoration(
    private val right: Int = 0,
    private val bottom: Int = 0,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            state.itemCount
            if (parent.layoutManager!!.layoutDirection == LinearLayoutManager.HORIZONTAL) {
                right = if(isLastItem(view, parent, state)) {
                    0
                } else {
                    this@MarginItemDecoration.right
                }
            } else {
                bottom = if(isLastItem(view, parent, state)) {
                    0
                } else {
                    this@MarginItemDecoration.bottom
                }
            }
        }
    }

    private fun isLastItem(view: View, parent: RecyclerView, state: RecyclerView.State)
        = parent.getChildAdapterPosition(view) == state.itemCount - 1

}