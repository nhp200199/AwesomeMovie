package vn.com.phucars.awesomemovies.ui.register

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import vn.com.phucars.awesomemovies.CoroutineTestRule
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.authentication.AuthUser
import vn.com.phucars.awesomemovies.data.common.exception.AuthEmailMalformedException
import vn.com.phucars.awesomemovies.data.common.exception.AuthInvalidEmailException
import vn.com.phucars.awesomemovies.data.common.exception.AuthInvalidPasswordException
import vn.com.phucars.awesomemovies.data.common.exception.AuthUserCollisionException
import vn.com.phucars.awesomemovies.data.common.exception.AuthWeakPasswordException
import vn.com.phucars.awesomemovies.data.common.exception.UnknownException
import vn.com.phucars.awesomemovies.domain.authentication.LoginUserUseCase
import vn.com.phucars.awesomemovies.testdata.AuthDataTest
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_CORRECT_EMAIL
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_INCORRECT_EMAIL
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_SHORT_PASSWORD
import vn.com.phucars.awesomemovies.ui.ResultViewState

private enum class LoginErrors {
    UNKNOWN,
    INVALID_EMAIL,
    INVALID_PASSWORD
}

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule(StandardTestDispatcher(
        TestCoroutineScheduler()
    ))
    private lateinit var SUT: LoginViewModel
    private lateinit var loginUserUseCase: LoginUserUseCase

    @Before
    fun setup() {
        loginUserUseCase = mockk<LoginUserUseCase>()
        SUT = LoginViewModel(loginUserUseCase, coroutineTestRule.testDispatcherProvider)
        success()
    }

    private fun success() {
        coEvery {
            loginUserUseCase(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)
        }.returns(
            ResultData.Success("123")
        )
    }

    @Test
    fun formStateFlow_emailFieldIsClear_incorrectEmailFormatErrorsReturned() = runTest {
        SUT.formValidationStateFlow.test {
            awaitItem()

            SUT.setEmailInput(REGISTER_CORRECT_EMAIL)
            SUT.setEmailInput("")
            awaitItem()
            assertThat(
                awaitItem(),
                `is`(
                    LoginFormUIState(
                        false, listOf(AuthorizationUIError.INVALID_EMAIL_FORMAT),
                        emptyList()
                    )
                )
            )
        }
    }

    @Test
    fun formStateFlow_emailFieldIsIncorrect_incorrectEmailFormatErrorsReturned() = runTest {
        SUT.formValidationStateFlow.test {
            awaitItem()

            SUT.setEmailInput(REGISTER_INCORRECT_EMAIL)
            assertThat(
                awaitItem(),
                `is`(
                    LoginFormUIState(
                        false,
                        listOf(AuthorizationUIError.INVALID_EMAIL_FORMAT),
                        emptyList()
                    )
                )
            )
        }
    }

    @Test
    fun formStateFlow_emailIsInCorrectFormat_emptyEmailError() = runTest {
        SUT.formValidationStateFlow.test {
            awaitItem()
            SUT.setEmailInput(REGISTER_CORRECT_EMAIL)

            assertThat(
                awaitItem(),
                `is`(LoginFormUIState(false, emptyList(), emptyList()))
            )
        }
    }

    @Test
    fun formStateFlow_passwordIsLessThan8Characters_shortPasswordError() = runTest {
        SUT.formValidationStateFlow.test {
            awaitItem()
            SUT.setPasswordInput(REGISTER_SHORT_PASSWORD)

            assertThat(
                awaitItem(), `is`(
                    LoginFormUIState(
                        false, emptyList(),
                        listOf(
                            AuthorizationUIError.PASSWORD_TOO_SHORT
                        ),
                    )
                )
            )
        }
    }

    @Test
    fun login_correctEmailAndPassword() = runTest {
        val email = slot<String>()
        val password = slot<String>()

        SUT.login(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)

        advanceUntilIdle()

        coVerify(exactly = 1) {
           loginUserUseCase(capture(email), capture(password))
        }

        assertThat(email.captured, `is`(AuthDataTest.EMAIL))
        assertThat(password.captured, `is`(AuthDataTest.PASSWORD))
    }

    @Test
    fun login_success_successReturned() = runTest {
        SUT.loginStateFlow.test {
            awaitItem()
            SUT.login(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)

            assertThat(awaitItem(), `is`(instanceOf(ResultViewState.Loading::class.java)))
            assertThat(awaitItem(), `is`(instanceOf(ResultViewState.Success::class.java)))
        }
    }


    @Test
    fun login_emailNotRegisteredError_invalidEmailReturned() = runTest {
        generateError(LoginErrors.INVALID_EMAIL)

        SUT.loginStateFlow.test {
            awaitItem()
            SUT.login(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)
            awaitItem()

            val loginResultState = awaitItem()

            assertThat(
                loginResultState,
                `is`(instanceOf(ResultViewState.Error::class.java))
            )
            assertThat(
                (loginResultState as ResultViewState.Error).exception,
                `is`(instanceOf(AuthInvalidEmailException::class.java))
            )
        }
    }

    @Test
    fun login_invalidPasswordError_invalidPasswordReturned() = runTest {
        generateError(LoginErrors.INVALID_PASSWORD)

        SUT.loginStateFlow.test {
            awaitItem()
            SUT.login(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)
            awaitItem()

            val result = awaitItem()
            assertThat(
                result,
                `is`(instanceOf(ResultViewState.Error::class.java))
            )
            assertThat(
                (result as ResultViewState.Error).exception,
                `is`(instanceOf(AuthInvalidPasswordException::class.java))
            )
        }
    }

    @Test
    fun login_generalError_generalErrorReturned() = runTest {
        generateError(LoginErrors.UNKNOWN)

        SUT.loginStateFlow.test {
            awaitItem()
            SUT.login(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)
            awaitItem()

            val result = awaitItem()
            assertThat(
                result,
                `is`(instanceOf(ResultViewState.Error::class.java))
            )
        }
    }

    private fun generateError(errorType: LoginErrors) {
        val exception = when(errorType) {
            LoginErrors.UNKNOWN -> UnknownException()
            LoginErrors.INVALID_EMAIL -> AuthInvalidEmailException()
            LoginErrors.INVALID_PASSWORD -> AuthInvalidPasswordException()
        }
        val result =  ResultData.Error(exception)
        coEvery {
            loginUserUseCase(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)
        }.returns(
            result
        )
    }
}