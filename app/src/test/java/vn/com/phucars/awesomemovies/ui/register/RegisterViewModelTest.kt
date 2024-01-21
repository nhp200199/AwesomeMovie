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
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import vn.com.phucars.awesomemovies.CoroutineTestRule
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.authentication.AuthUser
import vn.com.phucars.awesomemovies.data.common.exception.AuthEmailMalformedException
import vn.com.phucars.awesomemovies.data.common.exception.AuthUserCollisionException
import vn.com.phucars.awesomemovies.data.common.exception.AuthWeakPasswordException
import vn.com.phucars.awesomemovies.data.common.exception.UnknownException
import vn.com.phucars.awesomemovies.domain.authentication.RegisterUseCase
import vn.com.phucars.awesomemovies.testdata.AuthDataTest
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_ANOTHER_CORRECT_PASSWORD
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_CORRECT_EMAIL
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_CORRECT_PASSWORD
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_INCORRECT_EMAIL
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_NO_NUMBER_PASSWORD
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_NO_UPPERCASE_PASSWORD
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_SHORT_PASSWORD
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_WHITE_SPACE_PASSWORD
import vn.com.phucars.awesomemovies.ui.ResultViewState

private enum class RegistrationErrors {
    WEAK_PASSWORD,
    MALFORMED_EMAIL,
    COLLISION_USER,
    UNKNOWN
}

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {
    @get:Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule(
        StandardTestDispatcher(
            TestCoroutineScheduler()
        )
    )
    private lateinit var SUT: RegisterViewModel
    private lateinit var registerUseCase: RegisterUseCase

    @Before
    fun setup() {
        registerUseCase = mockk<RegisterUseCase>()
        SUT = RegisterViewModel(coroutineTestRule.testDispatcherProvider, registerUseCase)
        success()
    }

    private fun success() {
        coEvery {
            registerUseCase(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)
        }.returns(ResultData.Success("123"))
    }

    @Test
    fun register_correctEmailAndPassword() = runTest {
        val email = slot<String>()
        val password = slot<String>()

        SUT.register(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)
        advanceUntilIdle()

        coVerify(exactly = 1) {
            registerUseCase(capture(email), capture(password))
        }

        assertThat(AuthDataTest.EMAIL, `is`(AuthDataTest.EMAIL))
        assertThat(AuthDataTest.PASSWORD, `is`(AuthDataTest.PASSWORD))
    }

    @Test
    fun register_success_loadingReturned() = runTest {
        SUT.registerStateFlow.test {
            SUT.register(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)
            awaitItem()

            assertThat(awaitItem(), `is`(instanceOf(ResultViewState.Loading::class.java)))
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun register_success_successReturned() = runTest {
        SUT.registerStateFlow.test {
            SUT.register(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)
            awaitItem()
            awaitItem()

            assertThat(awaitItem(), `is`(instanceOf(ResultViewState.Success::class.java)))
        }
    }

    @Test
    fun register_weakPassword_weakPasswordErrorReturned() = runTest {
        generateError(RegistrationErrors.WEAK_PASSWORD)

        SUT.registerStateFlow.test {
            awaitItem()
            SUT.register(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)
            awaitItem()

            val result = awaitItem()
            assertThat(result, `is`(CoreMatchers.instanceOf(ResultViewState.Error::class.java)))
            assertThat((result as ResultViewState.Error).exception, `is`(instanceOf(AuthWeakPasswordException::class.java)))
        }
    }

    @Test
    fun register_malformedEmail_malformedEmailErrorReturned() = runTest {
        generateError(RegistrationErrors.MALFORMED_EMAIL)

        SUT.registerStateFlow.test {
            awaitItem()
            SUT.register(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)
            awaitItem()

            val result = awaitItem()
            assertThat(result, `is`(CoreMatchers.instanceOf(ResultViewState.Error::class.java)))
            assertThat((result as ResultViewState.Error).exception, `is`(instanceOf(AuthEmailMalformedException::class.java)))
        }
    }

    @Test
    fun register_collisionUser_collisionUserErrorReturned() = runTest {
        generateError(RegistrationErrors.COLLISION_USER)

        SUT.registerStateFlow.test {
            awaitItem()
            SUT.register(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)
            awaitItem()

            val result = awaitItem()
            assertThat(result, `is`(CoreMatchers.instanceOf(ResultViewState.Error::class.java)))
            assertThat((result as ResultViewState.Error).exception, `is`(instanceOf(AuthUserCollisionException::class.java)))
        }
    }

    @Test
    fun register_generalError_failureReturned() = runTest {
        generateError(RegistrationErrors.UNKNOWN)

        SUT.registerStateFlow.test {
            awaitItem()
            SUT.register(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)
            awaitItem()

            val result = awaitItem()
            assertThat(result, `is`(CoreMatchers.instanceOf(ResultViewState.Error::class.java)))
            assertThat((result as ResultViewState.Error).exception, `is`(instanceOf(UnknownException::class.java)))
        }
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
                `is`(RegisterFormUIState(false, listOf(AuthorizationUIError.INVALID_EMAIL_FORMAT),
                    emptyList(), emptyList()
                ))
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
                `is`(RegisterFormUIState(false, listOf(AuthorizationUIError.INVALID_EMAIL_FORMAT), emptyList(), emptyList()))
            )
        }
    }

    @Test
    fun formStateFlow_emailIsInCorrectFormat_emptyEmailError() = runTest {
        SUT.formValidationStateFlow.test {
            awaitItem()
            SUT.setEmailInput(REGISTER_CORRECT_EMAIL)

            assertThat(awaitItem(), `is`(RegisterFormUIState(false, emptyList(), emptyList(), emptyList())))
        }
    }

    @Test
    fun formStateFlow_passwordIsLessThan8Characters_shortPasswordError() = runTest {
        SUT.formValidationStateFlow.test {
            awaitItem()
            SUT.setPasswordInput(REGISTER_SHORT_PASSWORD)

            assertThat(awaitItem(), `is`(RegisterFormUIState(false, emptyList(),
                listOf(
                    AuthorizationUIError.PASSWORD_TOO_SHORT
                ),
                emptyList()
            )))
        }
    }

    @Test
    fun formStateFlow_passwordDoesNotHaveNumber_noNumberPasswordError() = runTest {
        SUT.formValidationStateFlow.test {
            awaitItem()
            SUT.setPasswordInput(REGISTER_NO_NUMBER_PASSWORD)

            assertThat(awaitItem(), `is`(RegisterFormUIState(false, emptyList(),
                listOf(
                    AuthorizationUIError.NO_NUMBER_PASSWORD
                ),
                emptyList()
            )))
        }
    }

    @Test
    fun formStateFlow_passwordDoesNotUppercase_noUppercasePasswordError() = runTest {
        SUT.formValidationStateFlow.test {
            awaitItem()
            SUT.setPasswordInput(REGISTER_NO_UPPERCASE_PASSWORD)

            assertThat(awaitItem(), `is`(RegisterFormUIState(false, emptyList(),
                listOf(
                    AuthorizationUIError.NO_UPPERCASE_PASSWORD
                ),
                emptyList()
            )))
        }
    }

    @Test
    fun formStateFlow_passwordContainsSpace_spacePasswordError() = runTest {
        SUT.formValidationStateFlow.test {
            awaitItem()
            SUT.setPasswordInput(REGISTER_WHITE_SPACE_PASSWORD)

            assertThat(awaitItem(), `is`(RegisterFormUIState(false, emptyList(),
                listOf(
                    AuthorizationUIError.WHITE_SPACE_PASSWORD
                ),
                emptyList()
            )))
        }
    }

    @Test
    fun formStateFlow_passwordIsCorrect_emptyPasswordError() = runTest {
        SUT.formValidationStateFlow.test {
            awaitItem()
            SUT.setPasswordInput(REGISTER_CORRECT_PASSWORD)

            assertThat(awaitItem(), `is`(RegisterFormUIState(false, emptyList(),
                emptyList(),
                emptyList()
            )))
        }
    }

    //repeat password is incorrect > incorrect password return
    @Test
    fun formStateFlow_repeatPasswordIsIncorrect_notPasswordError() = runTest {
        SUT.formValidationStateFlow.test {
            awaitItem()
            SUT.setPasswordInput(REGISTER_CORRECT_PASSWORD)
            awaitItem()
            SUT.setRepeatPasswordInput("Ph")

            assertThat(awaitItem(), `is`(RegisterFormUIState(false, emptyList(),
                emptyList(),
                listOf(AuthorizationUIError.UNMATCHED_REPEAT_PASSWORD)
            )))
        }
    }

    @Test
    @Ignore("Ignore for now. Come later when doing circular observable")
    fun formStateFlow_repeatPasswordCorrect_passwordChanged_unMatchedPasswordError() = runTest {
        SUT.formValidationStateFlow.test {
            awaitItem()
            SUT.setPasswordInput(REGISTER_CORRECT_PASSWORD)
            awaitItem()
            SUT.setRepeatPasswordInput(REGISTER_CORRECT_PASSWORD)
            awaitItem()

            SUT.setPasswordInput(REGISTER_ANOTHER_CORRECT_PASSWORD)

            assertThat(awaitItem(), `is`(RegisterFormUIState(false, emptyList(),
                emptyList(),
                listOf(AuthorizationUIError.UNMATCHED_REPEAT_PASSWORD)
            )))
        }
    }

    @Test
    fun formStateFlow_repeatPasswordIsCorrect_emptyRepeatPasswordError() = runTest {
        SUT.formValidationStateFlow.test {
            awaitItem()
            SUT.setPasswordInput(REGISTER_CORRECT_PASSWORD)
            awaitItem()
            SUT.setRepeatPasswordInput("Ph")
            awaitItem()
            SUT.setRepeatPasswordInput(REGISTER_CORRECT_PASSWORD)

            assertThat(awaitItem(), `is`(RegisterFormUIState(false, emptyList(),
                emptyList(),
                emptyList()
            )))
        }
    }

    @Test
    fun formStateFlow_allFieldsAreCorrect_emptyAllErrors() = runTest {
        SUT.formValidationStateFlow.test {
            awaitItem()
            SUT.setEmailInput(REGISTER_CORRECT_EMAIL)
            awaitItem()
            SUT.setPasswordInput(REGISTER_CORRECT_PASSWORD)
            awaitItem()
            SUT.setRepeatPasswordInput(REGISTER_CORRECT_PASSWORD)

            assertThat(awaitItem(), `is`(RegisterFormUIState(true, emptyList(),
                emptyList(),
                emptyList()
            )))
        }
    }

    private fun generateError(errorType: RegistrationErrors) {
        val exception = when(errorType) {
            RegistrationErrors.WEAK_PASSWORD -> AuthWeakPasswordException("")
            RegistrationErrors.MALFORMED_EMAIL -> AuthEmailMalformedException("")
            RegistrationErrors.COLLISION_USER -> AuthUserCollisionException("")
            RegistrationErrors.UNKNOWN -> UnknownException()
        }
        val result =  ResultData.Error(exception)
        coEvery {
            registerUseCase(AuthDataTest.EMAIL, AuthDataTest.PASSWORD)
        }.returns(
            result
        )
    }
}