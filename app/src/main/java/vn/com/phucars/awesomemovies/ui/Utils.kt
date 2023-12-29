package vn.com.phucars.awesomemovies.ui

import java.util.regex.Pattern

val EMAIL_ADDRESS_PATTERN = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)
fun isValidEmail(email: String): Boolean {
    return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
}
