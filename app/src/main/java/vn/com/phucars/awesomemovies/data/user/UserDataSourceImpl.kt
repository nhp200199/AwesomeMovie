package vn.com.phucars.awesomemovies.data.user

import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.common.exception.UnknownException
import vn.com.phucars.awesomemovies.dispatcher.DispatcherProvider
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class UserDataSourceImpl @Inject constructor(
    private val firebaseDatabase: DatabaseReference,
    private val dispatcherProvider: DispatcherProvider,
    private val sharedPreferences: SharedPreferences): UserDataSource {

    private val USER_TABLE = "user"
    private val PREF_UID = "uid"

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

    override suspend fun getUID(): String {
        return sharedPreferences.getString(PREF_UID, "")!!
    }

    override suspend fun userDataFlow(uid: String): Flow<User> {
        return callbackFlow {
            val userReference = firebaseDatabase.child(USER_TABLE).child(uid)
            val userListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("UserDataSourceImpl", "snapshot = $snapshot")
                    val user = snapshot.getValue(User::class.java)
                    user?.let {
                        Log.d("UserDataSourceImpl", "can cast to user object")
                        trySend(user)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("UserDataSourceImpl", "onCancelled = ${error.message}")
                }

            }
            userReference.addValueEventListener(userListener)

            awaitClose { userReference.removeEventListener(userListener) }
        }
    }

    override suspend fun setUID(uid: String) {
        sharedPreferences.edit().putString(PREF_UID, uid).apply()
    }
}