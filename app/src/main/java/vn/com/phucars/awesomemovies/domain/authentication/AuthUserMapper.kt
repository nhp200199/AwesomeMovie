package vn.com.phucars.awesomemovies.domain.authentication

import com.google.firebase.auth.FirebaseUser
import vn.com.phucars.awesomemovies.data.authentication.AuthUser
import vn.com.phucars.awesomemovies.mapper.Mapper

class AuthUserMapper : Mapper<FirebaseUser, String> {
    override fun map(input: FirebaseUser): String {
        return input.uid
    }
}