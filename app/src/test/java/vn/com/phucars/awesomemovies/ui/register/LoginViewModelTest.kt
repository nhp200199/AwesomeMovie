package vn.com.phucars.awesomemovies.ui.register

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_CORRECT_EMAIL
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_INCORRECT_EMAIL
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_SHORT_PASSWORD

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private lateinit var SUT: LoginViewModel

    @Before
    fun setup() {
        SUT = LoginViewModel()
    }

    @Test
    fun formStateFlow_emailFieldIsClear_incorrectEmailFormatErrorsReturned() = runTest {
        SUT.formValidationStateFlow.test {
            awaitItem()

            SUT.setEmailInput(REGISTER_CORRECT_EMAIL)
            SUT.setEmailInput("")
            awaitItem()
            MatcherAssert.assertThat(
                awaitItem(),
                CoreMatchers.`is`(
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
            MatcherAssert.assertThat(
                awaitItem(),
                CoreMatchers.`is`(
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

            MatcherAssert.assertThat(
                awaitItem(),
                CoreMatchers.`is`(LoginFormUIState(false, emptyList(), emptyList()))
            )
        }
    }

    @Test
    fun formStateFlow_passwordIsLessThan8Characters_shortPasswordError() = runTest {
        SUT.formValidationStateFlow.test {
            awaitItem()
            SUT.setPasswordInput(REGISTER_SHORT_PASSWORD)

            MatcherAssert.assertThat(
                awaitItem(), CoreMatchers.`is`(
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
}