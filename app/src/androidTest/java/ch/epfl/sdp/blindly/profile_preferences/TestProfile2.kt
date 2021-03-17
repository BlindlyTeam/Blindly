package ch.epfl.sdp.blindly.profile_preferences

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.times
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.Profile.Profile2
import ch.epfl.sdp.blindly.Profile.Profile3
import ch.epfl.sdp.blindly.R
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TestProfile2 {


    @get:Rule
    val activityRule = ActivityScenarioRule(Profile2::class.java)

    private val correctName = "Alice"
    private val incorrectShortName = "A"
    private val incorrectLongName = "abcdefabcdefabcdefabcdef"
    private val errorLongName = "Name can't be more than 20 characters!"
    private val errorShortName = "Name can't be less than 2 characters!"

    @Test
    fun testProfile2FiresProfile3() {
        Intents.init()
        Espresso.onView(withId(R.id.text_first_name)).perform(ViewActions.clearText(), ViewActions.typeText(correctName));
        Espresso.closeSoftKeyboard();
        val buttonStart = Espresso.onView(withId(R.id.button_p2))
        buttonStart.perform(click())
        intended(hasComponent(Profile3::class.java.name))
        Intents.release()
    }

    @Test
    fun shortNameOutputsError() {
        Intents.init()
        Espresso.onView(withId(R.id.text_first_name)).perform(ViewActions.clearText(), ViewActions.typeText(incorrectShortName));
        Espresso.closeSoftKeyboard();
        val buttonStart = Espresso.onView(withId(R.id.button_p2))
        buttonStart.perform(click())
        onView(withId(R.id.warning1_p2)).check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString(errorShortName))));
        intended(hasComponent(Profile3::class.java.name), times(0))
        Intents.release()
    }

    @Test
    fun longNameOutputsError() {
        Intents.init()
        Espresso.onView(withId(R.id.text_first_name)).perform(ViewActions.clearText(), ViewActions.typeText(incorrectLongName));
        Espresso.closeSoftKeyboard();
        val buttonStart = Espresso.onView(withId(R.id.button_p2))
        buttonStart.perform(click())
        onView(withId(R.id.warning2_p2)).check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString(errorLongName))));
        intended(hasComponent(Profile3::class.java.name), times(0))
        Intents.release()
    }

}