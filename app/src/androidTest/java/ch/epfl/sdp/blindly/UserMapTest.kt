package ch.epfl.sdp.blindly

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import ch.epfl.sdp.blindly.UserMapActivity.Companion.MATCH_ID
import ch.epfl.sdp.blindly.UserMapActivity.Companion.MATCH_NAME
import ch.epfl.sdp.blindly.helpers.BlindlyLatLng
import ch.epfl.sdp.blindly.helpers.DatatbaseHelper
import ch.epfl.sdp.blindly.helpers.Message
import ch.epfl.sdp.blindly.user.UserHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CompletableFuture
import javax.inject.Inject

const val OTHER_USER_ID = "other_user_id"
const val OTHER_USER_NAME = "An other user"

@HiltAndroidTest
class UserMapTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var user: UserHelper
    @Inject
    lateinit var database: DatatbaseHelper

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun openAndDisplayUserMap() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            UserMapActivity::class.java
        )

        ActivityScenario.launch<UserMapActivity>(intent)
        onView(withId(R.id.map)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    fun openAndDisplayUser() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            UserMapActivity::class.java
        )
        intent.putExtra(MATCH_ID, OTHER_USER_ID)
        ActivityScenario.launch<UserMapActivity>(intent)
        onView(withId(R.id.map)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }
    @Test
    fun openAndDisplayUserAndName() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            UserMapActivity::class.java
        )
        intent.putExtra(MATCH_ID, OTHER_USER_ID)
        intent.putExtra(MATCH_NAME, OTHER_USER_NAME)
        ActivityScenario.launch<UserMapActivity>(intent)
        onView(withId(R.id.map)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }
    @Test
    fun sendsLocation() {
        // Constant to check future constant completion
        val completed = "Location received"
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            UserMapActivity::class.java
        )
        intent.putExtra(MATCH_ID, OTHER_USER_ID)
        intent.putExtra(MATCH_NAME, OTHER_USER_NAME)
        ActivityScenario.launch<UserMapActivity>(intent)

        MatcherAssert.assertThat("Can't get user id", user.getUserId() != null)
        // We wait for the callbacks to return as the test database resends the same message
        // but from the other user
        val future: CompletableFuture<String> = CompletableFuture()
        database.getLocationLiveDatabase(user.getUserId()!!, OTHER_USER_ID).addListener(object :
            DatatbaseHelper.BlindlyLiveDatabase.EventListener<BlindlyLatLng>() {
            override fun onMessageReceived(message: Message<BlindlyLatLng>) {
                future.complete(completed)
            }
            override fun onMessageUpdated(message: Message<BlindlyLatLng>) {
                future.complete(completed)
            }
        })
        assertEquals(completed, future.get());
    }

}