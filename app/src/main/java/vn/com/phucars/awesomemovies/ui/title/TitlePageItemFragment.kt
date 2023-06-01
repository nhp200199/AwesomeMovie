package vn.com.phucars.awesomemovies.ui.title

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import vn.com.phucars.awesomemovies.R
import vn.com.phucars.awesomemovies.databinding.FragmentTitlePageItemBinding
import vn.com.phucars.awesomemovies.ui.base.BaseFragment

private const val ARG_GENRE = "genre"

class TitlePageItemFragment : BaseFragment<FragmentTitlePageItemBinding>() {
    private var genre: String? = null

    override fun getClassTag(): String {
        return "TitlePageItemFragment - genre: $genre"
    }

    override fun getViewBindingClass(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTitlePageItemBinding {
        return FragmentTitlePageItemBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        binding.vpTitlePageContainer.text = genre
    }

    override fun setViewListener() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            genre = it.getString(ARG_GENRE)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            TitlePageItemFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_GENRE, param1)
                }
            }
    }
}