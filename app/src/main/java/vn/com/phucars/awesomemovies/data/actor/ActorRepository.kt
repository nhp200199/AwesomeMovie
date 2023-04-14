package vn.com.phucars.awesomemovies.data.actor

import vn.com.phucars.awesomemovies.data.ResultData


interface ActorRepository {
    suspend fun getActorById(id: String): ResultData<ActorData>
}