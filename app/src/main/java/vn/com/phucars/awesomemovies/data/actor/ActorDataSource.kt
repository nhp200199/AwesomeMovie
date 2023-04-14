package vn.com.phucars.awesomemovies.data.actor

import vn.com.phucars.awesomemovies.data.ResultData

interface ActorDataSource {
    fun getActorById(id: String): ResultData<ActorData>
}