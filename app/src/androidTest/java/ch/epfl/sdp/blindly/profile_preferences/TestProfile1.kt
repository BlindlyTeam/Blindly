package ch.epfl.sdp.blindly.profile_preferences

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.Profile.Profile1
import ch.epfl.sdp.blindly.Profile.Profile2
import ch.epfl.sdp.blindly.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TestProfile1 {


    @get:Rule
    val activityRule = ActivityScenarioRule(Profile1::class.java)


    @Test
    fun testProfile1FiresProfile2() {
        Intents.init()
        val buttonStart = Espresso.onView(withId(R.id.button_p1))
        buttonStart.perform(click())
        intended(hasComponent(Profile2::class.java.name))
        Intents.release()
    }

}