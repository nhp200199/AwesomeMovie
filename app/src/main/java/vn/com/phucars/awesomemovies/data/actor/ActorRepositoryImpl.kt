package vn.com.phucars.awesomemovies.data.actor

import vn.com.phucars.awesomemovies.data.ResultData

class ActorRepositoryImpl(private val actorDataSource: ActorDataSource) : ActorRepository {
    override suspend fun getActorById(id: String): ResultData<ActorData> {
       return actorDataSource.getActorById(id)
    }
}