package vn.com.phucars.awesomemovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import vn.com.phucars.awesomemovies.ui.title.TitleWithRatingViewModel

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var adapter: BaseRecyclerAdapter<GenreViewState, ItemGenreBinding>
    override fun getClassTag(): String {
        return "MainActivity"
    }

    override fun getViewBindingClass(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun setupView() {
        adapter = BaseRecyclerAdapter<GenreViewState, ItemGenreBinding>()
        adapter.setOnItemClickedListener { position, view ->
            viewModel.selectGenre(position)
        }

        binding.rcvGenres.apply {
            this.adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(MarginItemDecoration(right = resources.getDimension(R.dimen.default_margin_horizontal_list_item).toInt()))
        }
    }

    override fun setViewListener() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.genreViewStateFlow.collect {
                    Log.d(getClassTag(), "genre view state = $it")
                    when(it) {
                        is ResultViewState.Initial -> {}
                        is ResultViewState.Loading -> {
                            binding.pbMainGenre.visibility = View.VISIBLE
                            binding.tilesGenresContainer.visibility = View.GONE
                        }
                        is ResultViewState.Error -> {}
                        is ResultViewState.Success -> {
                            binding.pbMainGenre.visibility = View.GONE
                            binding.tilesGenresContainer.visibility = View.VISIBLE

                            adapter.update(it.data)
                        }
                    }
                }
            }
        }
    }
}