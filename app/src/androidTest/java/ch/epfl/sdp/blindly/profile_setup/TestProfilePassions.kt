package ch.epfl.sdp.blindly.profile_setup

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class TestProfilePassions {

    @get:Rule
    val activityRule = ActivityScenarioRule(ProfilePassions::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val ERROR_MESSAGE_1 = "Please select at least one!"
    private val ERROR_MESSAGE_2 = "You can not select more than 5!"

    @Test
    fun noInputShowsError() {
        Intents.init()
        val buttonContinue = onView(withId(R.id.button_p7))
        buttonContinue.perform(click())

        onView(withId(R.id.warning_p7_1))
                .check(
                        ViewAssertions.matches(
                                ViewMatchers.withText(
                                        Matchers.containsString(
                                                ERROR_MESSAGE_1
                                        )
                                )
                        )
                )
        intended(hasComponent(ProfileAudioRecording::class.java.name), Intents.times(0))
        Intents.release()
    }

    @Test
    fun moreThanAllowedInputShowsError() {
        Intents.init()

        onView(withId(R.id.chip10)).perform(click())
        onView(withId(R.id.chip11)).perform(click())
        onView(withId(R.id.chip12)).perform(click())
        onView(withId(R.id.chip13)).perform(click())
        onView(withId(R.id.chip14)).perform(click())
        onView(withId(R.id.chip15)).perform(click())

        val buttonContinue = onView(withId(R.id.button_p7))
        buttonContinue.perform(click())

        onView(withId(R.id.warning_p7_2))
                .check(
                        ViewAssertions.matches(
                                ViewMatchers.withText(
                                        Matchers.containsString(
                                                ERROR_MESSAGE_2
                                        )
                                )
                        )
                );
        intended(hasComponent(ProfileAudioRecording::class.java.name), Intents.times(0))
        Intents.release()

    }

    @Test
    fun correctInputsFiresProfileAudioRecording() {
        Intents.init()

        onView(withId(R.id.chip10)).perform(click())
        onView(withId(R.id.chip11)).perform(click())
        onView(withId(R.id.chip12)).perform(click())

        val buttonContinue = onView(withId(R.id.button_p7))
        buttonContinue.perform(click())

        intended(hasComponent(ProfileAudioRecording::class.java.name))
        Intents.release()
    }

}