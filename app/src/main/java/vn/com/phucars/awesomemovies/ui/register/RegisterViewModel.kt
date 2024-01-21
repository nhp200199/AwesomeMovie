package vn.com.phucars.awesomemovies.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.authentication.AuthUser
import vn.com.phucars.awesomemovies.dispatcher.DispatcherProvider
import vn.com.phucars.awesomemovies.domain.authentication.RegisterUseCase
import vn.com.phucars.awesomemovies.ui.ResultViewState
import vn.com.phucars.awesomemovies.ui.isValidEmail
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val emailInputStateFlow = MutableStateFlow<String?>(null)
    private val passwordStateFlow = MutableStateFlow<String?>(null)
    private val repeatPasswordStateFlow = MutableStateFlow<String?>(null)
    private val _registerStateFlow = MutableStateFlow<ResultViewState<*>>(ResultViewState.Initial)
    val registerStateFlow = _registerStateFlow.asStateFlow()

    fun register(email: String, password: String) {
        viewModelScope.launch(dispatcherProvider.main()) {
            _registerStateFlow.value = ResultViewState.Loading
            val registerResult = registerUseCase(email, password)
            when(registerResult) {
                is ResultData.Success -> _registerStateFlow.value = ResultViewState.Success(registerResult.data)
                is ResultData.Error -> _registerStateFlow.value = ResultViewState.Error(registerResult.exception)
            }
        }
    }

    private val emailValidStateFlow = emailInputStateFlow.map {
        val emailError = mutableListOf<AuthorizationUIError>()
        if (it == null) return@map null
        if (it.isEmpty() || !isValidEmail(it)) {
            emailError.add(AuthorizationUIError.INVALID_EMAIL_FORMAT)
        }
        emailError.toImmutableList()
    }
    private val passwordValidStateFlow = passwordStateFlow.map {
        val passwordError = mutableListOf<AuthorizationUIError>()
        if (it == null) return@map null
        if (it.isEmpty() || it.length <= 8) passwordError.add(AuthorizationUIError.PASSWORD_TOO_SHORT)
        if (!it.any { char -> char.isDigit() }) passwordError.add(AuthorizationUIError.NO_NUMBER_PASSWORD)
        if (!it.any { char -> char.isUpperCase() }) passwordError.add(AuthorizationUIError.NO_UPPERCASE_PASSWORD)
        if (it.any { char -> char.isWhitespace() }) passwordError.add(AuthorizationUIError.WHITE_SPACE_PASSWORD)
        passwordError.toImmutableList()
    }
    private val repeatPasswordErrorStateFlow = repeatPasswordStateFlow.map {
        val passwordError = mutableListOf<AuthorizationUIError>()
        if (it == null) return@map null
        if (it != passwordStateFlow.value) {
            passwordError.add(AuthorizationUIError.UNMATCHED_REPEAT_PASSWORD)
        }
        passwordError.toImmutableList()
    }


    val formValidationStateFlow = combine(emailValidStateFlow, passwordValidStateFlow, repeatPasswordErrorStateFlow) { emailFields, passwordFields, repeatFields ->
        val emailErrors = emailFields ?: emptyList<AuthorizationUIError>()
        val passwordErrors = passwordFields ?: emptyList<AuthorizationUIError>()
        val repeatPasswordErrors = repeatFields ?: emptyList<AuthorizationUIError>()
        if (emailFields == null && passwordFields == null && repeatFields == null) {
            RegisterFormUIState(false, emailErrors, passwordErrors, repeatPasswordErrors)
        } else {
            val isEmailValid = emailFields != null && emailFields.isEmpty()
            val isPasswordValid = passwordFields != null && passwordFields.isEmpty()
            val isFormValid = isEmailValid && isPasswordValid
            RegisterFormUIState(isFormValid, emailErrors, passwordErrors, repeatPasswordErrors)
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

    fun setRepeatPasswordInput(repeatPassword: String) {
        repeatPasswordStateFlow.value = repeatPassword
    }
}