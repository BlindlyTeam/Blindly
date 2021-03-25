package ch.epfl.sdp.blindly.profile_preferences

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.BundleMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.profile.EXTRA_SHOW_ME
import ch.epfl.sdp.blindly.profile.Profile6
import ch.epfl.sdp.blindly.profile.Profile7
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

const val TEST_SHOW_ME = "Woman"
@RunWith(AndroidJUnit4::class)
class TestProfile6 {

    @get:Rule
    val activityRule = ActivityScenarioRule(Profile6::class.java)

    private val NO_INPUT_ERROR = "Please select one!"


    @Test
    fun noInputOutputsError() {
        Intents.init()
        val buttonContinue = Espresso.onView(withId(R.id.button_p6))
        buttonContinue.perform(click())
        intended(hasComponent(Profile7::class.java.name), Intents.times(0))
        Espresso.onView(withId(R.id.warning_p6))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.withText(
                        Matchers.containsString(
                            NO_INPUT_ERROR
                        )
                    )
                )
            );
        Intents.release()
    }

    @Test
    fun anyChoiceFiresProfile7() {
        Intents.init()
        val buttonPref = Espresso.onView(withId(R.id.sex1_pref))
        buttonPref.perform(click())
        val buttonContinue = Espresso.onView(withId(R.id.button_p6))
        buttonContinue.perform(click())
        intended(Matchers.allOf(hasComponent(Profile7::class.java.name), IntentMatchers.hasExtras(BundleMatchers.hasEntry(EXTRA_SHOW_ME, TEST_SHOW_ME))))
        Intents.release()
    }
}