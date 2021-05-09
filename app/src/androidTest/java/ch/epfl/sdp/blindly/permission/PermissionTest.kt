package ch.epfl.sdp.blindly.permission

import androidx.test.espresso.Espresso
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import ch.epfl.sdp.blindly.R
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
        .grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

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
        val buttonContinue = Espresso.onView(ViewMatchers.withId(R.id.enable_location_button))
        buttonContinue.perform(ViewActions.click())
        try { // Optionally double click, because apparently permisssion are not granted immediately
            buttonContinue.perform(ViewActions.click())
        } catch (e: NoMatchingViewException) {
            // If we can't click its not a problem, it only means that it has already been granted
        }
        Intents.intended(IntentMatchers.hasComponent(ProfileName::class.java.name))
    }
}