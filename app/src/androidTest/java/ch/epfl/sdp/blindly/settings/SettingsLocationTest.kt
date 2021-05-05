package ch.epfl.sdp.blindly.settings

import android.content.Intent
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.fake_module.FakeUserCacheModule.Companion.fakeUser
import ch.epfl.sdp.blindly.user.UserCache
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.UserRepository
import com.google.android.gms.maps.MapView
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

private const val TEST_LOCATION = "Lausanne, Switzerland"

@HiltAndroidTest

class SettingsLocationTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(Settings::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userHelper: UserHelper

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var userCache: UserCache

    @Inject
    lateinit var db: FirebaseFirestore

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun locationTextIsDisplayedCorrectly() {
        val TEST_USER_LOCATION = "Ecublens, Switzerland" //from the fakeUser
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), SettingsLocation::class.java)
        intent.putExtra(EXTRA_LOCATION, TEST_USER_LOCATION)

        val act = ActivityScenario.launch<SettingsLocation>(intent)
        var location: String? = null
        act.onActivity {
            location = it.findViewById<TextView>(R.id.my_current).text.toString()
        }
        assertThat(location, equalTo(TEST_USER_LOCATION))
    }


    @Test
    fun mapZoomsInWhenDoubleTapping() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), SettingsLocation::class.java)
        intent.putExtra(EXTRA_LOCATION, TEST_LOCATION)

        ActivityScenario.launch<SettingsLocation>(intent)

        Thread.sleep(1000)
        onView(withId(R.id.map)).perform(click(), click())
        Thread.sleep(1000)
    }
}