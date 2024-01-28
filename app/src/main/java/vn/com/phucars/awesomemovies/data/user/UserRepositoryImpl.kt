package vn.com.phucars.awesomemovies.data.user

import android.util.Log
import kotlinx.coroutines.flow.Flow
import vn.com.phucars.awesomemovies.data.ResultData
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDataSource: UserDataSource) : UserRepository {
    override suspend fun createUser(userId: String, user: User): ResultData<*> {
        return userDataSource.createUser(userId, user)
    }

    override suspend fun userDataFlow(): Flow<User> {
        return userDataSource.userDataFlow(userDataSource.getUID())
    }

    override suspend fun setUID(uid: String) {
        userDataSource.setUID(uid)
    }
}