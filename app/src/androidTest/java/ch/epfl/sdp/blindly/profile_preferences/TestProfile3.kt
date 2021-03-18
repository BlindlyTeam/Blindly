package ch.epfl.sdp.blindly.profile_preferences

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.Profile3
import ch.epfl.sdp.blindly.Profile4
import ch.epfl.sdp.blindly.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TestProfile3 {


    @get:Rule
    val activityRule = ActivityScenarioRule(Profile3::class.java)


    @Test
    fun testProfile3FiresProfile4() {
        Intents.init()
        val buttonStart = Espresso.onView(withId(R.id.button_p3))
        buttonStart.perform(click())
        intended(hasComponent(Profile4::class.java.name))
        Intents.release()
    }

}