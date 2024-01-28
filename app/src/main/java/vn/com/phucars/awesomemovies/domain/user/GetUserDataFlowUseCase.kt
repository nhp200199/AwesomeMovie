package vn.com.phucars.awesomemovies.domain.user

import android.util.Log
import kotlinx.coroutines.flow.Flow
import vn.com.phucars.awesomemovies.data.user.User
import vn.com.phucars.awesomemovies.data.user.UserRepository
import javax.inject.Inject

class GetUserDataFlowUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Flow<User> {
        return userRepository.userDataFlow()
    }
}