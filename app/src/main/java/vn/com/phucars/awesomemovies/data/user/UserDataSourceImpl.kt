package vn.com.phucars.awesomemovies.data.user

import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.withContext
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.common.exception.UnknownException
import vn.com.phucars.awesomemovies.dispatcher.DispatcherProvider
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class UserDataSourceImpl @Inject constructor(
    private val firebaseDatabase: DatabaseReference,
    private val dispatcherProvider: DispatcherProvider): UserDataSource {

    private val USER_TABLE = "user"

    override suspend fun createUser(userId: String, user: User): ResultData<*> {
        return withContext(dispatcherProvider.io()) {
            suspendCoroutine { cont ->
                firebaseDatabase.child(USER_TABLE).child(userId).setValue(user)
                    .addOnSuccessListener {
                        cont.resumeWith(Result.success(ResultData.Success(Any())))
                    }
                    .addOnFailureListener {
                        cont.resumeWith(Result.success(ResultData.Error(UnknownException())))
                    }
            }
        }
    }
}