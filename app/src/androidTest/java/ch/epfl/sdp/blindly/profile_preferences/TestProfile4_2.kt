package ch.epfl.sdp.blindly.profile_preferences

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.times
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.profile.Profile4_2
import ch.epfl.sdp.blindly.profile.Profile5
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TestProfile4_2 {

    @get:Rule
    val activityRule = ActivityScenarioRule(Profile4_2::class.java)

    private val CORRECT_SPECIFICATION = "Abcde"
    private val BLANK_SPECIFICATION = "   "
    private val NO_INPUT = ""
    private val ERROR_MESSAGE = "Please specify!"

    @Test
    fun noInputOutputsError() {
        Intents.init()
        onView(withId(R.id.text_p4_2)).perform(clearText(), typeText(NO_INPUT));
        closeSoftKeyboard();
        val buttonContinue = Espresso.onView(withId(R.id.button_p4_2))
        buttonContinue.perform(click())
        Espresso.onView(withId(R.id.warning_p4_2))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.withText(
                        Matchers.containsString(
                            ERROR_MESSAGE
                        )
                    )
                )
            );
        intended(hasComponent(Profile5::class.java.name), times(0))
        Intents.release()
    }


    @Test
    fun blankInputOutputsError() {
        Intents.init()
        onView(withId(R.id.text_p4_2)).perform(clearText(), typeText(BLANK_SPECIFICATION));
        closeSoftKeyboard();
        val buttonContinue = Espresso.onView(withId(R.id.button_p4_2))
        buttonContinue.perform(click())
        Espresso.onView(withId(R.id.warning_p4_2))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.withText(
                        Matchers.containsString(
                            ERROR_MESSAGE
                        )
                    )
                )
            );
        intended(hasComponent(Profile5::class.java.name), times(0))
        Intents.release()
    }

    @Test
    fun correctInputFiresProfile5() {
        Intents.init()
        onView(withId(R.id.text_p4_2)).perform(clearText(), typeText(CORRECT_SPECIFICATION));
        closeSoftKeyboard();
        val buttonContinue = Espresso.onView(withId(R.id.button_p4_2))
        buttonContinue.perform(click())
        intended(hasComponent(Profile5::class.java.name))
        Intents.release()
    }

}