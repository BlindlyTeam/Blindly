package ch.epfl.sdp.blindly.profile_preferences

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.Profile.Profile4
import ch.epfl.sdp.blindly.Profile.Profile4_2
import ch.epfl.sdp.blindly.Profile.Profile5
import ch.epfl.sdp.blindly.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TestProfile4 {


    @get:Rule
    val activityRule = ActivityScenarioRule(Profile4::class.java)


    @Test
    fun testProfile4FiresProfile5() {
        Intents.init()
        val buttonStart = Espresso.onView(withId(R.id.button_p4))
        buttonStart.perform(click())
        intended(hasComponent(Profile5::class.java.name))
        Intents.release()
    }

    @Test
    fun testProfile4FiresProfile4_2() {
        Intents.init()
        val buttonStart = Espresso.onView(withId(R.id.sex3_pref))
        buttonStart.perform(click())
        intended(hasComponent(Profile4_2::class.java.name))
        Intents.release()
    }



}