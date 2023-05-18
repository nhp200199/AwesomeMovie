package vn.com.phucars.awesomemovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import vn.com.phucars.awesomemovies.ui.AppViewModelFactory
import vn.com.phucars.awesomemovies.ui.title.TitleWithRatingViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: TitleWithRatingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, AppViewModelFactory())[TitleWithRatingViewModel::class.java]

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.titleWithRatingFlow.collect {

                }
            }
        }

        viewModel.initialize()
    }
}