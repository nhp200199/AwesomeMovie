package vn.com.phucars.awesomemovies.ui.register

data class LoginFormUIState(
    val isFormValid: Boolean,
    val emailErrorState: List<AuthorizationUIError>,
    val passwordErrorState: List<AuthorizationUIError>,
)