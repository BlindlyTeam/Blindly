package ch.epfl.sdp.blindly.profile_setup

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.times
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
class TestProfileBirthday {


    @get:Rule
    val activityRule = ActivityScenarioRule(ProfileBirthday::class.java)

    private val ERROR_AGE = "The minimum age requirement is 18 years old."

    @Test
    fun minorAgeOutputsError() {
        Intents.init()
        onView(withId(R.id.datePicker)).perform(PickerActions.setDate(2005, 3, 20));
        val buttonContinue = onView(withId(R.id.button_p3))
        buttonContinue.perform(click())
        intended(hasComponent(ProfileGender::class.java.name), times(0))
        onView(withId(R.id.warning_p3)).check(
                ViewAssertions.matches(
                        ViewMatchers.withText(
                                Matchers.containsString(
                                        ERROR_AGE
                                )
                        )
                )
        );
        Intents.release()
    }

    @Test
    fun adultAgeFiresProfileGender() {
        Intents.init()
        onView(withId(R.id.datePicker)).perform(PickerActions.setDate(2003, 3, 18));
        val buttonContinue = onView(withId(R.id.button_p3))
        buttonContinue.perform(click())
        intended(hasComponent(ProfileGender::class.java.name))
        Intents.release()
    }


}