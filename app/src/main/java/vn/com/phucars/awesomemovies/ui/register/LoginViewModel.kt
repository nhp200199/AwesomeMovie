package vn.com.phucars.awesomemovies.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.data.authentication.AuthUser
import vn.com.phucars.awesomemovies.dispatcher.DispatcherProvider
import vn.com.phucars.awesomemovies.domain.authentication.LoginUserUseCase
import vn.com.phucars.awesomemovies.ui.ResultViewState
import vn.com.phucars.awesomemovies.ui.isValidEmail
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val emailInputStateFlow = MutableStateFlow<String?>(null)
    private val passwordStateFlow = MutableStateFlow<String?>(null)
    private val _loginStateFlow = MutableStateFlow<ResultViewState<Any>>(ResultViewState.Initial)
    val loginStateFlow = _loginStateFlow.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch(dispatcherProvider.main()) {
            _loginStateFlow.value = ResultViewState.Loading
            val loginResult = loginUserUseCase(email, password)
            when(loginResult) {
                is ResultData.Success -> _loginStateFlow.value = ResultViewState.Success<AuthUser>(loginResult.data)
                is ResultData.Error -> _loginStateFlow.value = ResultViewState.Error(loginResult.exception)
            }
        }
    }

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