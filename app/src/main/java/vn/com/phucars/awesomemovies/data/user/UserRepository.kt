package vn.com.phucars.awesomemovies.data.user

import kotlinx.coroutines.flow.Flow
import vn.com.phucars.awesomemovies.data.ResultData

interface UserRepository {
    suspend fun createUser(userId: String, user: User): ResultData<*>
    suspend fun userDataFlow(): Flow<User>
    suspend fun setUID(uid: String)
}