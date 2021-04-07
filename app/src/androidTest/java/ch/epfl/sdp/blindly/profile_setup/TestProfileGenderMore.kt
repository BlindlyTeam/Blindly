package ch.epfl.sdp.blindly.profile_setup

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.times
import androidx.test.espresso.intent.matcher.BundleMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.R
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TestProfileGenderMore {

    @get:Rule
    val activityRule = ActivityScenarioRule(ProfileGenderMore::class.java)

    private val CORRECT_SPECIFICATION = "Abcde"
    private val BLANK_SPECIFICATION = "   "
    private val INCORRECT_CHARS_SPECIFICATION = "Abc;;de"
    private val NO_INPUT = ""
    private val ERROR_MESSAGE = "Please specify!"
    private val ERROR_CHARACTERS = "Please use only letters."

    @Test
    fun incorrectCharOutputsError() {
        Intents.init()
        onView(withId(R.id.text_p4_2)).perform(clearText(), typeText(INCORRECT_CHARS_SPECIFICATION));
        closeSoftKeyboard();
        val buttonContinue = onView(withId(R.id.button_p4_2))
        buttonContinue.perform(click())
        onView(withId(R.id.warning2_p4_2))
                .check(
                        ViewAssertions.matches(
                                ViewMatchers.withText(
                                        Matchers.containsString(
                                                ERROR_CHARACTERS
                                        )
                                )
                        )
                )
        intended(hasComponent(ProfileOrientation::class.java.name), times(0))
        Intents.release()
    }

    @Test
    fun noInputOutputsError() {
        Intents.init()
        onView(withId(R.id.text_p4_2)).perform(clearText(), typeText(NO_INPUT));
        closeSoftKeyboard();
        val buttonContinue = onView(withId(R.id.button_p4_2))
        buttonContinue.perform(click())
        onView(withId(R.id.warning1_p4_2))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.withText(
                        Matchers.containsString(ERROR_MESSAGE))))
        intended(hasComponent(ProfileOrientation::class.java.name), times(0))
        Intents.release()
    }


    @Test
    fun blankInputOutputsError() {
        Intents.init()
        onView(withId(R.id.text_p4_2)).perform(clearText(), typeText(BLANK_SPECIFICATION));
        closeSoftKeyboard();
        val buttonContinue = onView(withId(R.id.button_p4_2))
        buttonContinue.perform(click())
        onView(withId(R.id.warning1_p4_2))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.withText(
                        Matchers.containsString(ERROR_MESSAGE))))
        intended(hasComponent(ProfileOrientation::class.java.name), times(0))
        Intents.release()
    }

    @Test
    fun correctInputFiresProfileOrientation() {
        Intents.init()
        onView(withId(R.id.text_p4_2)).perform(clearText(), typeText(CORRECT_SPECIFICATION));
        closeSoftKeyboard();
        val buttonContinue = onView(withId(R.id.button_p4_2))
        buttonContinue.perform(click())
        intended(Matchers.allOf(hasComponent(ProfileOrientation::class.java.name),
                IntentMatchers.hasExtras(BundleMatchers.hasEntry(EXTRA_GENRE, CORRECT_SPECIFICATION))))
        Intents.release()
    }

}