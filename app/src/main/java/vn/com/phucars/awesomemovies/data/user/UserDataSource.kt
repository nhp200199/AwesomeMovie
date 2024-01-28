package vn.com.phucars.awesomemovies.data.user

import kotlinx.coroutines.flow.Flow
import vn.com.phucars.awesomemovies.data.ResultData

interface UserDataSource {
    suspend fun createUser(userId: String, user: User): ResultData<*>
    suspend fun getUID(): String
    suspend fun userDataFlow(uid: String): Flow<User>
    suspend fun setUID(uid: String)
}