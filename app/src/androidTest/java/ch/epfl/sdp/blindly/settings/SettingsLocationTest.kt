package ch.epfl.sdp.blindly.settings

import android.content.Intent
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.fake_module.FakeUserCacheModule.Companion.fakeUser
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.storage.UserCache
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.equalTo
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
        val act = launchSettingsLocation()

        var TEST_LOCATION: String? = null
        var location: String? = null
        act.onActivity { it ->
            val locSer = AndroidLocationService(ApplicationProvider.getApplicationContext())
            val loc = locSer.getCurrentLocation()
            location = it.findViewById<TextView>(R.id.my_current).text.toString()
            TEST_LOCATION = loc?.let {
                AndroidLocationService.getCurrentLocationStringFromLocation(
                    ApplicationProvider.getApplicationContext(), it
                )
            }
        }
        //Should take the newly computed location instead of the one from the user
        assertThat(location, equalTo(TEST_LOCATION))
    }

    private fun launchSettingsLocation(): ActivityScenario<SettingsLocation> {
        val TEST_USER_LOCATION = AndroidLocationService.getCurrentLocationStringFromUser(
            ApplicationProvider.getApplicationContext(),
            fakeUser
        )
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), SettingsLocation::class.java)
        intent.putExtra(EXTRA_LOCATION, TEST_USER_LOCATION)

        return ActivityScenario.launch(intent)
    }
}