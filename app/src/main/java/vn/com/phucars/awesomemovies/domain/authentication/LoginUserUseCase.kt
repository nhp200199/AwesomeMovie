package vn.com.phucars.awesomemovies.domain.authentication

import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.authentication.AuthRepository
import vn.com.phucars.awesomemovies.data.authentication.AuthUser
import vn.com.phucars.awesomemovies.data.user.UserRepository
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): ResultData<String> {
        val result = authRepository.login(email, password)
        if (result is ResultData.Success) {
            userRepository.setUID(result.data)
        }
        return authRepository.login(email, password)
    }
}