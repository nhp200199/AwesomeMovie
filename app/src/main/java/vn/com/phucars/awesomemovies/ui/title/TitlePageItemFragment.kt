package vn.com.phucars.awesomemovies.ui.title

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import vn.com.phucars.awesomemovies.R
import vn.com.phucars.awesomemovies.databinding.FragmentTitlePageItemBinding
import vn.com.phucars.awesomemovies.databinding.ItemTitleBinding
import vn.com.phucars.awesomemovies.ui.base.BaseFragment
import vn.com.phucars.awesomemovies.ui.base.adapter.BaseRecyclerAdapter
import vn.com.phucars.awesomemovies.ui.common.MarginItemDecoration

private const val ARG_GENRE = "genre"

@AndroidEntryPoint
class TitlePageItemFragment : BaseFragment<FragmentTitlePageItemBinding>() {
    private var genre: String? = null
    private lateinit var titleAdapter: BaseRecyclerAdapter<TitleWithRatingViewState, ItemTitleBinding>

    private val viewModel: TitlePageItemViewModel by viewModels()

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
        titleAdapter = BaseRecyclerAdapter()
        binding.rcvTitles.adapter = titleAdapter
        binding.rcvTitles.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rcvTitles.addItemDecoration(MarginItemDecoration(bottom = resources.getDimension(R.dimen.default_margin_vertical_list_item).toInt()))
    }

    override fun setViewListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.titleWithRatingFlow.collect {
                    Log.d(getClassTag(), it.toString())
                    titleAdapter.update(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadingStateFlow.collect {
                    binding.pbTitle.visibility = if (it) View.VISIBLE else View.GONE
                    binding.rcvTitles.visibility = if (it) View.GONE else View.VISIBLE
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            genre = it.getString(ARG_GENRE)
        }

        viewModel.fetchTitlesForGenre(genre!!)
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