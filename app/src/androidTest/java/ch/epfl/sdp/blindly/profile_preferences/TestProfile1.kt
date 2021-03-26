package ch.epfl.sdp.blindly.profile_preferences

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.LocationPermissionActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.profile.Profile1
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TestProfile1 {


    @get:Rule
    val activityRule = ActivityScenarioRule(Profile1::class.java)


    @Test
    fun testProfile1FiresLocationPermissionActivity() {
        Intents.init()
        val buttonContinue = Espresso.onView(withId(R.id.button_p1))
        buttonContinue.perform(click())
        intended(hasComponent(LocationPermissionActivity::class.java.name))
        Intents.release()
    }

}