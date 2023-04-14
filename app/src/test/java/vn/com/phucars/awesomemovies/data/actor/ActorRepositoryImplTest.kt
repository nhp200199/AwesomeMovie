package vn.com.phucars.awesomemovies.data.actor

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.stub
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.mockito.Mockito.*
import org.hamcrest.MatcherAssert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matcher
import org.junit.Test
import org.mockito.Mockito
import vn.com.phucars.awesomemovies.data.ResultData
import vn.com.phucars.awesomemovies.testdata.ActorDataTest

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class ActorRepositoryImplTest {
    lateinit var SUT: ActorRepositoryImpl
    lateinit var actorDataSource: ActorDataSource

    @Before
    fun setup() {
        actorDataSource = mock(ActorDataSource::class.java)
        SUT = ActorRepositoryImpl(actorDataSource)
    }

    @Test
    fun getActorById_success_ActorDataWithGivenIdReturned() = runTest {
        //arrange
        `when`(actorDataSource.getActorById(ActorDataTest.ACTOR_FRED_ID)).thenReturn(ResultData.Success<ActorData>(ActorDataTest.ACTOR_FRED))
        //action
        val actorData = SUT.getActorById(ActorDataTest.ACTOR_FRED_ID)
        //assert
        assertThat(actorData, `is`(instanceOf(ResultData.Success::class.java)))
        assertThat((actorData as ResultData.Success<ActorData>), notNullValue())
    }
}