package vn.com.phucars.awesomemovies.ui.register

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_CORRECT_EMAIL
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_CORRECT_PASSWORD
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_INCORRECT_EMAIL
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_NO_NUMBER_PASSWORD
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_NO_UPPERCASE_PASSWORD
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_SHORT_PASSWORD
import vn.com.phucars.awesomemovies.testdata.ui.REGISTER_WHITE_SPACE_PASSWORD

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {
    private lateinit var SUT: RegisterViewModel

    @Before
    fun setup() {
        SUT = RegisterViewModel()
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
}