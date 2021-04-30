package ch.epfl.sdp.blindly

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import ch.epfl.sdp.blindly.permissions.LocationPermissionActivity
import ch.epfl.sdp.blindly.profile_setup.ProfileName
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class PermissionTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LocationPermissionActivity::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Rule
    @JvmField
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule
        .grant(android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION)
    @Before
    fun setup() {
        hiltRule.inject()
        init()
    }

    @After
    fun afterEach() {
        release()
    }

    @Test
    fun testLocationPermissionFiresProfileName() {
        activityRule.scenario.onActivity { activity ->
            activity.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        }
        val buttonContinue = Espresso.onView(ViewMatchers.withId(R.id.enable_location_button))
        buttonContinue.perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(ProfileName::class.java.name))
    }

}
