package vn.com.phucars.awesomemovies.data.authentication

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.common.exception.AuthEmailMalformedException
import vn.com.phucars.awesomemovies.data.common.exception.AuthUserCollisionException
import vn.com.phucars.awesomemovies.data.common.exception.AuthWeakPasswordException
import vn.com.phucars.awesomemovies.testdata.AuthDataTest

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryImplTest {
    private lateinit var SUT: AuthRepositoryImpl
    private lateinit var authDataSource: AuthDataSource

    @Before
    fun setup() {
        authDataSource = mockk<AuthDataSource>()
        SUT = AuthRepositoryImpl(authDataSource)
        success()
    }

    @Test
    fun register_correctEmailAndPassword() = runTest {
        val email = slot<String>()
        val password = slot<String>()

        SUT.register(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)

        coVerify(exactly = 1) {
            authDataSource.register(capture(email), capture(password))
        }

        assertThat(AuthDataTest.EMAIL, `is`(AuthDataTest.EMAIL))
        assertThat(AuthDataTest.PASSWORD, `is`(AuthDataTest.PASSWORD))
    }

    @Test
    fun register_success_successReturned() = runTest {
        val result = SUT.register(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)

        assertThat(result, `is`(instanceOf(ResultData.Success::class.java)))
    }

    @Test
    fun register_weakPassword_weakPasswordErrorReturned() = runTest {
        weakPassword()

        val result = SUT.register(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)

        assertThat(result, `is`(instanceOf(ResultData.Error::class.java)))
        assertThat((result as ResultData.Error).exception, `is`(instanceOf(AuthWeakPasswordException::class.java)))
    }

    @Test
    fun register_malformedEmail_malformedEmailErrorReturned() = runTest {
        malformedEmail()

        val result = SUT.register(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)

        assertThat(result, `is`(instanceOf(ResultData.Error::class.java)))
        assertThat((result as ResultData.Error).exception, `is`(instanceOf(AuthEmailMalformedException::class.java)))
    }

    @Test
    fun register_collisionUser_collisionUserErrorReturned() = runTest {
        collisionUser()

        val result = SUT.register(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)

        assertThat(result, `is`(instanceOf(ResultData.Error::class.java)))
        assertThat((result as ResultData.Error).exception, `is`(instanceOf(AuthUserCollisionException::class.java)))
    }

    @Test
    fun register_generalError_failureReturned() = runTest {
        generalError()

        val result = SUT.register(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)

        assertThat(result, `is`(instanceOf(ResultData.Error::class.java)))
    }

    private fun success() {
        coEvery { authDataSource.register(AuthDataTest.EMAIL, AuthDataTest.PASSWORD) }
            .returns(ResultData.Success(AuthUser()))
    }

    private fun generalError() {
        coEvery { SUT.register(AuthDataTest.EMAIL, AuthDataTest.PASSWORD) }
            .returns(ResultData.Error(Exception()))
    }

    private fun malformedEmail() {
        coEvery { SUT.register(AuthDataTest.EMAIL, AuthDataTest.PASSWORD) }
            .returns(ResultData.Error(AuthEmailMalformedException("")))
    }

    private fun collisionUser() {
        coEvery { SUT.register(AuthDataTest.EMAIL, AuthDataTest.PASSWORD) }
            .returns(ResultData.Error(AuthUserCollisionException("")))
    }

    private fun weakPassword() {
        coEvery { SUT.register(AuthDataTest.EMAIL, AuthDataTest.PASSWORD) }
            .returns(ResultData.Error(AuthWeakPasswordException("")))
    }
}