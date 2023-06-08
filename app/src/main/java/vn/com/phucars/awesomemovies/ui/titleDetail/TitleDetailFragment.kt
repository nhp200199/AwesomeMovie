package vn.com.phucars.awesomemovies.ui.titleDetail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import androidx.core.view.marginLeft
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import vn.com.phucars.awesomemovies.R
import vn.com.phucars.awesomemovies.databinding.FragmentTitleDetailBinding
import vn.com.phucars.awesomemovies.databinding.ItemActorInTitleBinding
import vn.com.phucars.awesomemovies.ui.base.BaseFragment
import vn.com.phucars.awesomemovies.ui.base.adapter.BaseRecyclerAdapter
import vn.com.phucars.awesomemovies.ui.common.MarginItemDecoration
import vn.com.phucars.awesomemovies.ui.common.overrideColor
import vn.com.phucars.awesomemovies.ui.common.toPx

class TitleDetailFragment : BaseFragment<FragmentTitleDetailBinding>() {
    private val testObject = TitleDetailViewState(
        "Fast & Furious 9",
        listOf(
            "Action",
            "Crime",
            "Thriller"
        ),
        "https://m.media-amazon.com/images/M/MV5BMjI0NmFkYzEtNzU2YS00NTg5LWIwYmMtNmQ1MTU0OGJjOTMxXkEyXkFqcGdeQXVyMjMxOTE0ODA@._V1_.jpg",
        2021,
        "Dom and the crew must take on an international terrorist who turns out to be Dom and Mia's estranged brother.",
        listOf(
            Actor(
                "nm0004874",
                "https://m.media-amazon.com/images/M/MV5BMjExNzA4MDYxN15BMl5BanBnXkFtZTcwOTI1MDAxOQ@@._V1_.jpg",
                "Vin Diesel",
                "Dominic Toretto"
            ),
            Actor(
                "nm0735442",
                "https://m.media-amazon.com/images/M/MV5BMTkwODIzMzYyMl5BMl5BanBnXkFtZTYwNzAyNjAz._V1_.jpg",
                "Michelle Rodriguez",
                "Letty"
            ),
            Actor(
                "nm0108287",
                "https://m.media-amazon.com/images/M/MV5BMTc1OTMwMzM3NF5BMl5BanBnXkFtZTgwMTM5MzIyODE@._V1_.jpg",
                "Jordana Brewster",
                "Mia"
            ),
            Actor(
                "nm0879085",
                "https://m.media-amazon.com/images/M/MV5BMjA3MjU1NzY4OF5BMl5BanBnXkFtZTgwMzU3MDQxNTE@._V1_.jpg",
                "Tyrese Gibson",
                "Roman"
            ),
        ),
        5.2,
        146681,
        "8580"
    )

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
        binding.apply {
            Glide.with(requireContext())
                .load(testObject.posterUrl)
                .into(ivPoster)

            tvDurationContent.text = testObject.duration
            tvReleaseDateContent.text = testObject.releaseYear.toString()
            tvDescription.text = testObject.description
            tvRatingContent.text = "${testObject.rating} (${testObject.voteCount} votes)"

            binding.includeTb.toolbar.apply {
                title = testObject.title
                navigationIcon = resources.getDrawable(com.google.android.material.R.drawable.ic_arrow_back_black_24)
                setNavigationOnClickListener {
                    requireActivity().onBackPressed()
                }
            }

            for (genre in testObject.genres) {
                val tvGenre: TextView = setUpGenreTextForFlexLayout(genre)
                binding.flexGenres.addView(tvGenre)
            }
        }

        setupActorRcvView()
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
        drawable.overrideColor(resources.getColor(R.color.teal_700))
        tvGenre.background = drawable

        return tvGenre
    }

    private fun setupActorRcvView() {
        val adapter = BaseRecyclerAdapter<Actor, ItemActorInTitleBinding>(testObject.castings)
        binding.rcvCastings.apply {
            this.adapter = adapter
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

    }

    companion object {
        private const val ARG_TITLE_NAME = "TitleDetailFragment.TitleName"
        private const val ARG_TITLE_ID = "TitleDetailFragment.TitleId"

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