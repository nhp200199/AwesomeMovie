package vn.com.phucars.awesomemovies.ui.register

data class RegisterFormUIState(
    val isFormValid: Boolean,
    val emailErrorState: List<AuthorizationUIError>,
    val passwordErrorState: List<AuthorizationUIError>,
    val repeatPasswordErrorState: List<AuthorizationUIError>
)
