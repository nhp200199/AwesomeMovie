package vn.com.phucars.awesomemovies.ui.base.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import vn.com.phucars.awesomemovies.R
import vn.com.phucars.awesomemovies.ui.genre.GenreItemViewHolder
import vn.com.phucars.awesomemovies.ui.title.TitleItemViewHolder
import java.security.InvalidParameterException

class ViewHolderFactory {
    companion object {
        fun <T: ViewBinding> createViewHolder(layoutId: Int, parent: ViewGroup): BaseRecyclerViewHolder<T> {
            Log.d("ViewHolderFactory", "create view holder - layout id: $layoutId")
            val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            return when(layoutId) {
                R.layout.item_genre -> GenreItemViewHolder(view) as BaseRecyclerViewHolder<T>
                R.layout.item_title -> TitleItemViewHolder(view) as BaseRecyclerViewHolder<T>
                else -> throw InvalidParameterException("Cannot find view holder associated with layout id")
            }
        }
    }
}