package ch.epfl.sdp.blindly.profile_preferences

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.profile.Profile7
import ch.epfl.sdp.blindly.profile.Profile8
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestProfile7 {
    @get:Rule
    val activityRule = ActivityScenarioRule(Profile7::class.java)

    @Test
    fun testProfile7FiresProfile8() {
        Intents.init()
        val buttonContinue = Espresso.onView(ViewMatchers.withId(R.id.button_p7))
        buttonContinue.perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(Profile8::class.java.name))
        Intents.release()
    }
}