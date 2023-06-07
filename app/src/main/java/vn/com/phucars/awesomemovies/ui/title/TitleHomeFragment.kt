package vn.com.phucars.awesomemovies.ui.title

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import vn.com.phucars.awesomemovies.MainActivity
import vn.com.phucars.awesomemovies.R
import vn.com.phucars.awesomemovies.databinding.FragmentTitleHomeBinding
import vn.com.phucars.awesomemovies.databinding.ItemGenreBinding
import vn.com.phucars.awesomemovies.ui.ResultViewState
import vn.com.phucars.awesomemovies.ui.base.BaseFragment
import vn.com.phucars.awesomemovies.ui.base.adapter.BaseRecyclerAdapter
import vn.com.phucars.awesomemovies.ui.common.MarginItemDecoration
import vn.com.phucars.awesomemovies.ui.genre.GenreViewState

@AndroidEntryPoint
class TitleHomeFragment : BaseFragment<FragmentTitleHomeBinding>() {
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var genreAdapter: BaseRecyclerAdapter<GenreViewState, ItemGenreBinding>
    private lateinit var titlesPagerAdapter: TitlePagerAdapter

    private val pagerChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            viewModel.selectGenre(position)
        }
    }

    override fun getClassTag(): String {
        return TitleHomeFragment::class.java.simpleName
    }

    override fun getViewBindingClass(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTitleHomeBinding {
        return FragmentTitleHomeBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        setUpGenreAdapter()
        setUpTitlePageAdapter()
    }

    private fun setUpGenreAdapter() {
        genreAdapter = BaseRecyclerAdapter<GenreViewState, ItemGenreBinding>()
        genreAdapter.setOnItemClickedListener { position, view ->
            viewModel.selectGenre(position)
        }

        binding.rcvGenres.apply {
            this.adapter = this@TitleHomeFragment.genreAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(MarginItemDecoration(right = resources.getDimension(R.dimen.default_margin_horizontal_list_item).toInt()))
        }
    }

    private fun setUpTitlePageAdapter() {
        titlesPagerAdapter = TitlePagerAdapter(this)
        binding.vpTitlePageContainer.adapter = titlesPagerAdapter
        binding.vpTitlePageContainer.offscreenPageLimit = 2
    }

    override fun setViewListener() {
        binding.vpTitlePageContainer.registerOnPageChangeCallback(pagerChangeCallback)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.genreViewStateFlow.collect { it ->
                    Log.d(getClassTag(), "genre view state = $it")
                    when (it) {
                        is ResultViewState.Initial -> {}
                        is ResultViewState.Loading -> {
                            binding.pbMainGenre.visibility = View.VISIBLE
                            binding.tilesGenresContainer.visibility = View.GONE
                        }
                        is ResultViewState.Error -> {}
                        is ResultViewState.Success -> {
                            binding.pbMainGenre.visibility = View.GONE
                            binding.tilesGenresContainer.visibility = View.VISIBLE

                            genreAdapter.update(it.data)
                            titlesPagerAdapter.update(it.data.map { it.genre })
                            binding.vpTitlePageContainer.setCurrentItem(viewModel.getSelectedGenre(), false)
                            binding.rcvGenres.scrollToPosition(viewModel.getSelectedGenre())
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.vpTitlePageContainer.unregisterOnPageChangeCallback(pagerChangeCallback)
    }

    private inner class TitlePagerAdapter(
        fa: Fragment,
        private var genreList: List<String> = emptyList()
    ) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = genreList.size

        override fun createFragment(position: Int): Fragment {
            return TitlePageItemFragment.newInstance(genreList[position])
        }

        fun update(newGenreList: List<String>) {
            genreList = newGenreList
            notifyDataSetChanged()
        }
    }
}