package vn.com.phucars.awesomemovies.data.authentication

import vn.com.phucars.awesomemovies.data.ResultData

class AuthRepositoryImpl(private val authDataSource: AuthDataSource) : AuthRepository {
    override suspend fun register(email: String, password: String): ResultData<AuthUser> {
        return authDataSource.register(email, password)
    }

    override suspend fun login(email: String, password: String): ResultData<AuthUser> {
        return authDataSource.login(email, password)
    }
}