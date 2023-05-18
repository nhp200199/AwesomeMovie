package vn.com.phucars.awesomemovies.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vn.com.phucars.awesomemovies.data.common.remote.retrofit.MovieApiManager
import vn.com.phucars.awesomemovies.data.title.TitleRemoteDataSourceImpl
import vn.com.phucars.awesomemovies.data.title.TitleRepositoryImpl
import vn.com.phucars.awesomemovies.dispatcher.DefaultDispatcherProvider
import vn.com.phucars.awesomemovies.dispatcher.DispatcherProvider
import vn.com.phucars.awesomemovies.domain.title.GetTitleWithRatingListGroupByGenre
import vn.com.phucars.awesomemovies.domain.title.TitleWithRatingRemoteDtoToDomain
import vn.com.phucars.awesomemovies.ui.title.TitleWithRatingViewModel

class AppViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var viewModel: ViewModel? = null
        if (modelClass.isAssignableFrom(TitleWithRatingViewModel::class.java)) {
            viewModel = TitleWithRatingViewModel(
                GetTitleWithRatingListGroupByGenre(
                    TitleRepositoryImpl(
                        TitleRemoteDataSourceImpl(MovieApiManager.titleService),
                        TitleWithRatingRemoteDtoToDomain()
                    )
                ), DefaultDispatcherProvider()
            )
        } else throw IllegalArgumentException("Unknown ViewModel class")
        return viewModel as T
    }
}