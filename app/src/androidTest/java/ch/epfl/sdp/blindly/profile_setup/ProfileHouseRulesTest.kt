package ch.epfl.sdp.blindly.profile_setup

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.permissions.LocationPermissionActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ProfileHouseRulesTest {


    @get:Rule
    val activityRule = ActivityScenarioRule(ProfileHouseRules::class.java)

    @Before
    fun setup() {
        init()
    }

    @After
    fun afterEach() {
        release()
    }

    @Test
    fun testProfileHouseRulesFiresLocationPermissionActivity() {
        val buttonContinue = Espresso.onView(withId(R.id.button_p1))
        buttonContinue.perform(click())
        intended(hasComponent(LocationPermissionActivity::class.java.name))
    }

}