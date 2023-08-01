package vn.com.phucars.awesomemovies.ui.titleSearch

import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import vn.com.phucars.awesomemovies.R
import vn.com.phucars.awesomemovies.databinding.FragmentTitleSearchBinding
import vn.com.phucars.awesomemovies.ui.base.BaseFragment
import vn.com.phucars.awesomemovies.ui.common.MarginItemDecoration
import vn.com.phucars.awesomemovies.ui.title.DetailTitlePagingAdapter
import vn.com.phucars.awesomemovies.ui.titleDetail.TitleDetailFragment
import vn.com.phucars.awesomemovies.ui.titleDetail.TitleDetailViewState

@AndroidEntryPoint
class TitleSearchFragment : BaseFragment<FragmentTitleSearchBinding>() {
    private val viewModel: TitleSearchViewModel by viewModels()

    private lateinit var adapter: DetailTitlePagingAdapter

    override fun getClassTag(): String {
        return TitleSearchFragment::class.java.simpleName
    }

    override fun getViewBindingClass(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTitleSearchBinding {
        return FragmentTitleSearchBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        setupToolbar()
        setupRecyclerView()

        binding.edtSearchTitle.requestFocus()
    }

    private fun setupRecyclerView() {
        adapter = DetailTitlePagingAdapter(object :
            DiffUtil.ItemCallback<TitleDetailViewState>() {
            override fun areItemsTheSame(
                oldItem: TitleDetailViewState,
                newItem: TitleDetailViewState
            ): Boolean {
                return oldItem.areItemsTheSame(newItem)
            }

            override fun areContentsTheSame(
                oldItem: TitleDetailViewState,
                newItem: TitleDetailViewState
            ): Boolean {
                return oldItem.areContentsTheSame(newItem)
            }

        }) { item ->
            requireActivity().supportFragmentManager.commit {
                replace(R.id.main_container, TitleDetailFragment.newInstance(item.id, item.title))
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }

        binding.rcvTitlesResult.apply {
            this.adapter = this@TitleSearchFragment.adapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(MarginItemDecoration(bottom = resources.getDimension(R.dimen.default_margin_vertical_list_item).toInt()))
        }
    }

    private fun setupToolbar() {
        binding.searchToolbar.apply {
            navigationIcon =
                resources.getDrawable(com.google.android.material.R.drawable.ic_arrow_back_black_24)
                    .also {
                        it.setTint(resources.getColor(R.color.white))
                    }
            setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    override fun setViewListener() {
        binding.edtSearchTitle.setOnEditorActionListener(object : OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.searchForTitle(binding.edtSearchTitle.text.toString())
                    return true
                }
                return false
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.titleSearchResultFlow.collectLatest {
                    Log.d(getClassTag(), it.toString())
                    adapter.submitData(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow
                    .distinctUntilChangedBy { it.refresh }
                    .collectLatest {
                        Log.d(getClassTag(), "current load state: $it")
                        binding.pbTitleSearch.isVisible = it.refresh is LoadState.Loading
                        binding.rcvTitlesResult.isVisible = it.refresh !is LoadState.Loading
                        binding.tvLoadDetailError.isVisible = it.refresh is LoadState.Error
                    }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = TitleSearchFragment()
    }
}