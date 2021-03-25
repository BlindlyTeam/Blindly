package ch.epfl.sdp.blindly

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.profile.Profile2
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class PermissionTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LocationPermissionActivity::class.java)

    @Test
    fun testLocationPermissionFiresProfile2() {
        init()
        val buttonContinue = Espresso.onView(ViewMatchers.withId(R.id.button))
        buttonContinue.perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(Profile2::class.java.name))
        release()
    }

}