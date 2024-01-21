package vn.com.phucars.awesomemovies.data.user

import vn.com.phucars.awesomemovies.data.ResultData

interface UserDataSource {
    suspend fun createUser(userId: String, user: User): ResultData<*>
}