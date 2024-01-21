package vn.com.phucars.awesomemovies.data.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.common.exception.AuthEmailMalformedException
import vn.com.phucars.awesomemovies.data.common.exception.AuthInvalidEmailException
import vn.com.phucars.awesomemovies.data.common.exception.AuthInvalidPasswordException
import vn.com.phucars.awesomemovies.data.common.exception.AuthUserCollisionException
import vn.com.phucars.awesomemovies.data.common.exception.AuthWeakPasswordException
import vn.com.phucars.awesomemovies.data.common.exception.UnknownException
import vn.com.phucars.awesomemovies.dispatcher.DispatcherProvider
import vn.com.phucars.awesomemovies.mapper.Mapper
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class AuthDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userMapper: Mapper<FirebaseUser, String>,
    private val dispatcherProvider: DispatcherProvider
) : AuthDataSource {
    override suspend fun register(email: String, password: String): ResultData<String> {
        return withContext(dispatcherProvider.io()) {
            suspendCoroutine<ResultData<String>> { cont ->
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            cont.resumeWith(Result.success(ResultData.Success(userMapper.map(firebaseAuth.currentUser!!))))
                        } else {
                            val exception: Exception = when(task.exception) {
                                is FirebaseAuthWeakPasswordException -> {
                                    AuthWeakPasswordException((task.exception as FirebaseAuthWeakPasswordException).localizedMessage!!)
                                }
                                is FirebaseAuthInvalidCredentialsException -> {
                                    AuthEmailMalformedException((task.exception as FirebaseAuthInvalidCredentialsException).localizedMessage!!)
                                }
                                is FirebaseAuthUserCollisionException -> {
                                    AuthUserCollisionException((task.exception as FirebaseAuthUserCollisionException).localizedMessage!!)
                                }
                                else -> {
                                    UnknownException()
                                }
                            }
                            cont.resumeWith(Result.success(ResultData.Error(exception)))
                        }
                    }
            }
        }
    }

    override suspend fun login(email: String, password: String): ResultData<String> {
        return withContext(dispatcherProvider.io()) {
            suspendCoroutine<ResultData<String>> { cont ->
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            firebaseAuth.currentUser
                            cont.resumeWith(Result.success(ResultData.Success(userMapper.map(firebaseAuth.currentUser!!))))
                        } else {
                            val exception: Exception = when(task.exception) {
                                is FirebaseAuthInvalidUserException -> {
                                    AuthInvalidEmailException()
                                }
                                is FirebaseAuthInvalidCredentialsException -> {
                                    AuthInvalidPasswordException()
                                }
                                else -> {
                                    UnknownException()
                                }
                            }
                            cont.resumeWith(Result.success(ResultData.Error(exception)))
                        }
                    }
            }
        }
    }

    override suspend fun logout() {
        withContext(dispatcherProvider.io()) {
            firebaseAuth.signOut()
        }
    }
}