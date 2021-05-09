package ch.epfl.sdp.blindly.map

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import ch.epfl.sdp.blindly.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import androidx.test.espresso.matcher.ViewMatchers.*
import ch.epfl.sdp.blindly.chat.ChatActivity.Companion.MATCH_ID
import ch.epfl.sdp.blindly.helpers.BlindlyLatLng
import ch.epfl.sdp.blindly.helpers.DatatbaseHelper
import ch.epfl.sdp.blindly.helpers.Message
import ch.epfl.sdp.blindly.map.UserMapActivity.Companion.MATCH_NAME
import ch.epfl.sdp.blindly.user.UserHelper
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CompletableFuture
import javax.inject.Inject

const val OTHER_USER_ID = "other_user_id"
const val OTHER_USER_NAME = "An other user"
const val FUTURE_COMPLETED = "An other user"

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
        onView(withId(R.id.map)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun openAndDisplayUser() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            UserMapActivity::class.java
        )
        intent.putExtra(MATCH_ID, OTHER_USER_ID)
        ActivityScenario.launch<UserMapActivity>(intent)
        onView(withId(R.id.map)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
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
        onView(withId(R.id.map)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
    @Test
    fun activitySendsLocation() {
        // Constant to check future constant completion
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
                future.complete(FUTURE_COMPLETED)
            }
            override fun onMessageUpdated(message: Message<BlindlyLatLng>) {
                future.complete(FUTURE_COMPLETED)
            }
        })
        assertThat(future.get(), equalTo(FUTURE_COMPLETED))
    }
    @Test
    fun openAndDisplayUserWithoutPoints() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), UserMapActivity::class.java)
    }

    @Test
    fun receiveInvalidLocation() {
        // Constant to check future constant completion
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            UserMapActivity::class.java
        )
        intent.putExtra(MATCH_ID, OTHER_USER_ID)
        intent.putExtra(MATCH_NAME, OTHER_USER_NAME)
        val scenario = ActivityScenario.launch<UserMapActivity>(intent)

        MatcherAssert.assertThat("Can't get user id", user.getUserId() != null)

        val liveDb = database.getLocationLiveDatabase(user.getUserId()!!, OTHER_USER_ID)
        val future: CompletableFuture<String> = CompletableFuture()
        scenario.onActivity {
            // We wait for the callbacks on the main thread to completes. By registering later
            // and being on the main thread we ensure that the is no race conditions
            liveDb.addListener(object :
                DatatbaseHelper.BlindlyLiveDatabase.EventListener<BlindlyLatLng>() {
                override fun onMessageReceived(message: Message<BlindlyLatLng>) {
                    future.complete(FUTURE_COMPLETED)
                }

                override fun onMessageUpdated(message: Message<BlindlyLatLng>) {
                    future.complete(FUTURE_COMPLETED)
                }
            })
            // Invalid data
            liveDb.updateLocation(BlindlyLatLng())
        }
        // We wait for the future to complete without other assert, because
        // we want to check that the activity doesn't crash
        assertThat(future.get(), equalTo(FUTURE_COMPLETED))
    }
}