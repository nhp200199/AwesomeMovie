package vn.com.phucars.awesomemovies.data.authentication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.common.exception.AuthEmailMalformedException
import vn.com.phucars.awesomemovies.data.common.exception.AuthInvalidUserEmailException
import vn.com.phucars.awesomemovies.data.common.exception.AuthInvalidUserPasswordException
import vn.com.phucars.awesomemovies.data.common.exception.AuthUserCollisionException
import vn.com.phucars.awesomemovies.data.common.exception.AuthWeakPasswordException
import vn.com.phucars.awesomemovies.data.common.exception.UnknownException
import vn.com.phucars.awesomemovies.mapper.Mapper
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthDataSourceImpl(
    private val firebaseAuth: FirebaseAuth,
    private val userMapper: Mapper<FirebaseUser, AuthUser>,
    private val dispatcher: CoroutineDispatcher
) : AuthDataSource {
    override suspend fun register(email: String, password: String): ResultData<AuthUser> {
        return withContext(dispatcher) {
            suspendCoroutine<ResultData<AuthUser>> { cont ->
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            firebaseAuth.currentUser
                            cont.resumeWith(Result.success(ResultData.Success(userMapper.map(firebaseAuth.currentUser!!))))
                        } else {
                            val exception: Exception = when(task.exception) {
                                is FirebaseAuthWeakPasswordException -> {
                                    AuthWeakPasswordException((task.exception as FirebaseAuthWeakPasswordException).localizedMessage!!)
                                }
                                is FirebaseAuthInvalidCredentialsException -> {
                                    AuthEmailMalformedException((task.exception as AuthEmailMalformedException).localizedMessage!!)
                                }
                                is FirebaseAuthUserCollisionException -> {
                                    AuthUserCollisionException((task.exception as AuthUserCollisionException).localizedMessage!!)
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

    override suspend fun login(email: String, password: String): ResultData<AuthUser> {
        return withContext(dispatcher) {
            suspendCoroutine<ResultData<AuthUser>> { cont ->
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            firebaseAuth.currentUser
                            cont.resumeWith(Result.success(ResultData.Success(userMapper.map(firebaseAuth.currentUser!!))))
                        } else {
                            val exception: Exception = when(task.exception) {
                                is FirebaseAuthInvalidUserException -> {
                                    AuthInvalidUserEmailException()
                                }
                                is FirebaseAuthInvalidCredentialsException -> {
                                    AuthInvalidUserPasswordException()
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
        withContext(dispatcher) {
            firebaseAuth.signOut()
        }
    }
}