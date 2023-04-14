package vn.com.phucars.awesomemovies.testdata

import vn.com.phucars.awesomemovies.data.actor.ActorData

class ActorDataTest {
    companion object {
        val ACTOR_FRED_ID = "nm0000001"
        val ACTOR_FRED = ActorData(
            "nm0000001",
            "Fred Astaire",
            1899,
            1987,
            "soundtrack,actor,miscellaneous",
            "tt0072308,tt0050419,tt0053137,tt0045537"
        )
    }
}