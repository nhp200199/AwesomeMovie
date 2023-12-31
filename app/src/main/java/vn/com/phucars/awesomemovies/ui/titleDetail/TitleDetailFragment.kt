package vn.com.phucars.awesomemovies.ui.titleDetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.view.marginLeft
import androidx.core.view.setPadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import vn.com.phucars.awesomemovies.R
import vn.com.phucars.awesomemovies.data.title.source.remote.TitleRemoteDataSource
import vn.com.phucars.awesomemovies.databinding.FragmentTitleDetailBinding
import vn.com.phucars.awesomemovies.databinding.ItemActorInTitleBinding
import vn.com.phucars.awesomemovies.ui.ResultViewState
import vn.com.phucars.awesomemovies.ui.base.BaseFragment
import vn.com.phucars.awesomemovies.ui.base.adapter.BaseRecyclerAdapter
import vn.com.phucars.awesomemovies.ui.common.MarginItemDecoration
import vn.com.phucars.awesomemovies.ui.common.overrideColor
import vn.com.phucars.awesomemovies.ui.common.toPx

@AndroidEntryPoint
class TitleDetailFragment : BaseFragment<FragmentTitleDetailBinding>() {
    private val viewModel: TitleDetailViewModel by viewModels()
    private lateinit var titleId: String
    private lateinit var titleName: String
    private val actorAdapter = BaseRecyclerAdapter<Actor, ItemActorInTitleBinding>()

    override fun getClassTag(): String {
        return TitleDetailFragment::class.java.simpleName
    }

    override fun getViewBindingClass(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTitleDetailBinding {
        return FragmentTitleDetailBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        binding.includeTb.toolbar.apply {
            title = this@TitleDetailFragment.titleName
            navigationIcon = resources.getDrawable(com.google.android.material.R.drawable.ic_arrow_back_black_24).also {
                it.setTint(resources.getColor(R.color.white))
            }
            setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }

        setupActorRcvView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            titleId = it.getString(ARG_TITLE_ID, "")
            titleName = it.getString(ARG_TITLE_NAME, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.init(titleId)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun updateTitleDetailView(titleDetail: TitleDetailViewState) {
        //clear all existing genre view first
        binding.flexGenres.removeAllViews()

        binding.apply {
            Glide.with(requireContext())
                .load(titleDetail.posterUrl)
                .into(ivPoster)

            tvDurationContent.text = titleDetail.duration
            tvReleaseDateContent.text = titleDetail.releaseYear.toString()
            tvDescription.text = titleDetail.description
            tvRatingContent.text = "${titleDetail.rating} (${titleDetail.voteCount} votes)"

            actorAdapter.update(titleDetail.castings)

            for (genre in titleDetail.genres) {
                val tvGenre: TextView = setUpGenreTextForFlexLayout(genre)
                binding.flexGenres.addView(tvGenre)
            }
        }
    }

    private fun setUpGenreTextForFlexLayout(genre: String): TextView {
        val tvGenre = TextView(requireContext())
        val paddingInDp = 8
        tvGenre.setPadding(paddingInDp.toPx.toInt())
        tvGenre.marginLeft

        tvGenre.text = genre
        tvGenre.setTextColor(resources.getColor(R.color.white))

        val drawable = resources.getDrawable(R.drawable.round_black_bg)
            .constantState!!
            .newDrawable(resources)
            .mutate()
        drawable.overrideColor(resources.getColor(R.color.teal_400))
        tvGenre.background = drawable

        return tvGenre
    }

    private fun setupActorRcvView() {
        binding.rcvCastings.apply {
            this.adapter = this@TitleDetailFragment.actorAdapter
            this.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.addItemDecoration(
                MarginItemDecoration(
                    right = resources.getDimension(R.dimen.default_margin_horizontal_list_item)
                        .toInt()
                )
            )
        }
    }

    override fun setViewListener() {
        binding.swlRefreshTitleDetail.setOnRefreshListener {
            viewModel.refresh()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.titleDetailFlow.collect {
                    Log.d(getClassTag(), it.toString())
                    if (it != ResultViewState.Loading) {
                        binding.swlRefreshTitleDetail.isRefreshing = false
                    }

                    when(it) {
                        is ResultViewState.Error -> {
                            binding.tvLoadDetailError.isVisible = true
                            binding.titleInfoDetailContainer.isVisible = false
                            binding.pbTitleDetail.isVisible = false
                        }
                        ResultViewState.Initial -> {}
                        ResultViewState.Loading -> {
                            binding.pbTitleDetail.isVisible = true
                            binding.titleInfoDetailContainer.isVisible = false
                            binding.tvLoadDetailError.isVisible = false
                        }
                        is ResultViewState.Success -> {
                            binding.titleInfoDetailContainer.isVisible = true
                            binding.pbTitleDetail.isVisible = false
                            binding.tvLoadDetailError.isVisible = false

                            updateTitleDetailView(it.data)
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val ARG_TITLE_NAME = "TitleDetailFragment.TitleName"
        const val ARG_TITLE_ID = "TitleDetailFragment.TitleId"

        @JvmStatic
        fun newInstance(titleId: String, titleName: String) =
            TitleDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE_ID, titleId)
                    putString(ARG_TITLE_NAME, titleName)
                }
            }
    }
}