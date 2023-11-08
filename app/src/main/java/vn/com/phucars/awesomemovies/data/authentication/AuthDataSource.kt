package vn.com.phucars.awesomemovies.data.authentication

import vn.com.phucars.awesomemovies.data.ResultData

interface AuthDataSource {
    suspend fun register(email: String, password: String): ResultData<AuthUser>
}
