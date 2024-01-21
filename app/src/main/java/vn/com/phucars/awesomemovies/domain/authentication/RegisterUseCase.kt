package vn.com.phucars.awesomemovies.domain.authentication

import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.authentication.AuthRepository
import vn.com.phucars.awesomemovies.data.authentication.AuthUser
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String): ResultData<String> {
        return authRepository.register(email, password)
    }
}