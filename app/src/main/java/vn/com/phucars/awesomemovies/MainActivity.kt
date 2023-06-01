package vn.com.phucars.awesomemovies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import vn.com.phucars.awesomemovies.databinding.ActivityMainBinding
import vn.com.phucars.awesomemovies.databinding.ItemGenreBinding
import vn.com.phucars.awesomemovies.ui.ResultViewState
import vn.com.phucars.awesomemovies.ui.base.BaseActivity
import vn.com.phucars.awesomemovies.ui.base.adapter.BaseRecyclerAdapter
import vn.com.phucars.awesomemovies.ui.common.MarginItemDecoration
import vn.com.phucars.awesomemovies.ui.genre.GenreViewState
import vn.com.phucars.awesomemovies.ui.title.MainActivityViewModel
import vn.com.phucars.awesomemovies.ui.title.TitlePageItemFragment

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var genreAdapter: BaseRecyclerAdapter<GenreViewState, ItemGenreBinding>
    private lateinit var titlesPagerAdapter: TitlePagerAdapter

    private val pagerChangeCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            viewModel.selectGenre(position)
        }
    }

    override fun getClassTag(): String {
        return "MainActivity"
    }

    override fun getViewBindingClass(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
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
            this.adapter = this@MainActivity.genreAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
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

    override fun onDestroy() {
        super.onDestroy()
        binding.vpTitlePageContainer.unregisterOnPageChangeCallback(pagerChangeCallback)
    }

    private inner class TitlePagerAdapter(
        fa: FragmentActivity,
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