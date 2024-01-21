package vn.com.phucars.awesomemovies.data.authentication

import vn.com.phucars.awesomemovies.data.ResultData
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authDataSource: AuthDataSource) : AuthRepository {
    override suspend fun register(email: String, password: String): ResultData<String> {
        return authDataSource.register(email, password)
    }

    override suspend fun login(email: String, password: String): ResultData<String> {
        return authDataSource.login(email, password)
    }
}