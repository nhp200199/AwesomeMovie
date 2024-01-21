package vn.com.phucars.awesomemovies.domain.authentication

import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.authentication.AuthRepository
import vn.com.phucars.awesomemovies.data.authentication.AuthUser
import vn.com.phucars.awesomemovies.data.user.User
import vn.com.phucars.awesomemovies.data.user.UserRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(email: String, password: String): ResultData<*> {
        val registerResult = authRepository.register(email, password)
        if (registerResult is ResultData.Success) {
            val user = User(registerResult.data, email)
            return userRepository.createUser(registerResult.data, user)
        }
        return registerResult
    }
}