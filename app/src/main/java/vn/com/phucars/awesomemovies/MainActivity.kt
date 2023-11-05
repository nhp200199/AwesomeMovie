package vn.com.phucars.awesomemovies

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import vn.com.phucars.awesomemovies.databinding.ActivityMainBinding
import vn.com.phucars.awesomemovies.ui.base.BaseActivity
import vn.com.phucars.awesomemovies.ui.title.TitleHomeFragment

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getClassTag(): String {
        return "MainActivity"
    }

    override fun getViewBindingClass(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun setupView() {}

    override fun setViewListener() {}

}