package vn.com.phucars.awesomemovies.data.user

import com.google.firebase.database.Exclude

data class User(
    @get:Exclude val id: String?,
    val email: String?
)
