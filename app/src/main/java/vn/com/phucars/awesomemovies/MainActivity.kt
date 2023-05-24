package vn.com.phucars.awesomemovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import vn.com.phucars.awesomemovies.ui.title.TitleWithRatingViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: TitleWithRatingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.titleWithRatingFlow.collect {

                }
            }
        }

        viewModel.initialize()
    }
}