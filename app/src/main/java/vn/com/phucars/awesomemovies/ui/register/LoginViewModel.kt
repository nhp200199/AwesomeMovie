package vn.com.phucars.awesomemovies.ui.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import okhttp3.internal.toImmutableList
import vn.com.phucars.awesomemovies.ui.isValidEmail

class LoginViewModel : ViewModel() {
    private val emailInputStateFlow = MutableStateFlow<String?>(null)
    private val passwordStateFlow = MutableStateFlow<String?>(null)

    private val emailErrorStateFlow = emailInputStateFlow.map {
        val emailError = mutableListOf<AuthorizationUIError>()
        if (it == null) return@map null
        if (it.isEmpty() || !isValidEmail(it)) {
            emailError.add(AuthorizationUIError.INVALID_EMAIL_FORMAT)
        }
        emailError.toImmutableList()
    }
    private val passwordErrorStateFlow = passwordStateFlow.map {
        val passwordError = mutableListOf<AuthorizationUIError>()
        if (it == null) return@map null
        if (it.isEmpty() || it.length <= 8) passwordError.add(AuthorizationUIError.PASSWORD_TOO_SHORT)
        passwordError.toImmutableList()
    }

    val formValidationStateFlow = combine(emailErrorStateFlow, passwordErrorStateFlow) { emailFields, passwordFields ->
        val emailErrors = emailFields ?: emptyList<AuthorizationUIError>()
        val passwordErrors = passwordFields ?: emptyList<AuthorizationUIError>()
        if (emailFields == null && passwordFields == null) {
            LoginFormUIState(false, emailErrors, passwordErrors)
        } else {
            val isEmailValid = emailFields != null && emailFields.isEmpty()
            val isPasswordValid = passwordFields != null && passwordFields.isEmpty()
            val isFormValid = isEmailValid && isPasswordValid
            LoginFormUIState(isFormValid, emailErrors, passwordErrors)
        }
    }


//        viewModelScope.async {
//        combine(emailValidStateFlow, passwordValidStateFlow) { emailFields, passwordFields  ->
//            RegisterFieldsState(emailFields, passwordFields)
//        }.fold(RegisterFormUIState(false, emptyMap())) {
//            a, b -> RegisterFormUIState(false, emptyMap())
//        }
//    }

//        .stateIn(
//            scope,
//            SharingStarted.WhileSubscribed(5000L),
//            false
//        )

    fun setEmailInput(email: String) {
        emailInputStateFlow.value = email
    }

    fun setPasswordInput(password: String) {
        passwordStateFlow.value = password
    }
}