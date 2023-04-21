package vn.com.phucars.awesomemovies

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import vn.com.phucars.awesomemovies.dispatcher.DispatcherProvider

@OptIn(ExperimentalCoroutinesApi::class)
class StandardTestDispatcherProvider: DispatcherProvider {
    override fun main(): CoroutineDispatcher {
        return StandardTestDispatcher()
    }

    override fun default(): CoroutineDispatcher {
        return StandardTestDispatcher()
    }

    override fun io(): CoroutineDispatcher {
        return StandardTestDispatcher()
    }

    override fun unconfined(): CoroutineDispatcher {
        return StandardTestDispatcher()
    }
}