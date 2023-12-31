package vn.com.phucars.awesomemovies.ui.title

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import vn.com.phucars.awesomemovies.R
import vn.com.phucars.awesomemovies.databinding.FragmentTitlePageItemBinding
import vn.com.phucars.awesomemovies.ui.base.BaseFragment
import vn.com.phucars.awesomemovies.ui.common.MarginItemDecoration
import vn.com.phucars.awesomemovies.ui.titleDetail.TitleDetailFragment

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

        }) { item ->
            val navController = findNavController()
            val bundle = bundleOf(
                TitleDetailFragment.ARG_TITLE_ID to item.id,
                TitleDetailFragment.ARG_TITLE_NAME to item.titleText
            )
            navController.navigate(R.id.action_titleHomeFragment_to_titleDetailFragment, bundle)
        }
        binding.rcvTitles.adapter = titleAdapter
            .withLoadStateFooter(TitleLoadStateAdapter() {
                titleAdapter.retry()
            })
        binding.rcvTitles.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rcvTitles.addItemDecoration(MarginItemDecoration(bottom = resources.getDimension(R.dimen.default_margin_vertical_list_item).toInt()))
    }

    override fun setViewListener() {
        binding.btnRetry.setOnClickListener {
            titleAdapter.retry()
        }

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
                titleAdapter.loadStateFlow
                    .distinctUntilChangedBy { it.refresh }
                    .collectLatest {
                        Log.d(getClassTag(), "current load state: $it")
                    binding.pbTitle.isVisible = it.refresh is LoadState.Loading
                    binding.rcvTitles.isVisible = it.refresh !is LoadState.Loading
                    binding.llRetryContainer.isVisible = it.refresh is LoadState.Error
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