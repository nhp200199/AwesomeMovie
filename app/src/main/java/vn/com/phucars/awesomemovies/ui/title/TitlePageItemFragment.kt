package vn.com.phucars.awesomemovies.ui.title

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import vn.com.phucars.awesomemovies.R
import vn.com.phucars.awesomemovies.databinding.FragmentTitlePageItemBinding
import vn.com.phucars.awesomemovies.ui.base.BaseFragment
import vn.com.phucars.awesomemovies.ui.base.adapter.BaseRecyclerAdapter
import vn.com.phucars.awesomemovies.ui.common.MarginItemDecoration

private const val ARG_GENRE = "genre"

@AndroidEntryPoint
class TitlePageItemFragment : BaseFragment<FragmentTitlePageItemBinding>() {
    private var genre: String? = null
    private lateinit var titleAdapter: TitleWithRatingPagingAdapter

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
        titleAdapter = TitleWithRatingPagingAdapter(object :
            DiffUtil.ItemCallback<TitleWithRatingViewState>() {
            override fun areItemsTheSame(
                oldItem: TitleWithRatingViewState,
                newItem: TitleWithRatingViewState
            ): Boolean {
                return oldItem.areItemsTheSame(newItem)
            }

            override fun areContentsTheSame(
                oldItem: TitleWithRatingViewState,
                newItem: TitleWithRatingViewState
            ): Boolean {
                return oldItem.areContentsTheSame(newItem)
            }

        })
        binding.rcvTitles.adapter = titleAdapter
            .withLoadStateFooter(TitleLoadStateAdapter())
        binding.rcvTitles.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rcvTitles.addItemDecoration(MarginItemDecoration(bottom = resources.getDimension(R.dimen.default_margin_vertical_list_item).toInt()))
    }

    override fun setViewListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.titleWithRatingFlow.collectLatest {
                    Log.d(getClassTag(), it.toString())
                    titleAdapter.submitData(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                titleAdapter.loadStateFlow.collectLatest {
                    binding.pbTitle.isVisible = it.refresh is LoadState.Loading
                    binding.rcvTitles.isVisible = it.refresh !is LoadState.Loading
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