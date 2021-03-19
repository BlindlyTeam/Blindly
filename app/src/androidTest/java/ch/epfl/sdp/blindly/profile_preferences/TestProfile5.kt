package ch.epfl.sdp.blindly.profile_preferences

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.profile.Profile5
import ch.epfl.sdp.blindly.profile.Profile6
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TestProfile5 {


    @get:Rule
    val activityRule = ActivityScenarioRule(Profile5::class.java)

    private val ERROR_MESSAGE_1 = "Please select at least one!"
    private val ERROR_MESSAGE_2 = "You can not select more than 3!"

    @Test
    fun noInputOutputsError() {
        Intents.init()
        val buttonContinue = Espresso.onView(withId(R.id.button_p5))
        buttonContinue.perform(click())

        Espresso.onView(withId(R.id.warning_p5_1))
                .check(
                        ViewAssertions.matches(
                                ViewMatchers.withText(
                                        Matchers.containsString(
                                                ERROR_MESSAGE_1
                                        )
                                )
                        )
                );
        intended(hasComponent(Profile6::class.java.name), Intents.times(0))
        Intents.release()
    }

    @Test
    fun moreThanAllowedInputOutputsError() {
        Intents.init()
        val chip1 = Espresso.onView(withId(R.id.chip1))
        val chip2 = Espresso.onView(withId(R.id.chip2))
        val chip3 = Espresso.onView(withId(R.id.chip3))
        val chip4 = Espresso.onView(withId(R.id.chip4))
        chip1.perform(click())
        chip2.perform(click())
        chip3.perform(click())
        chip4.perform(click())

        val buttonContinue = Espresso.onView(withId(R.id.button_p5))
        buttonContinue.perform(click())

        Espresso.onView(withId(R.id.warning_p5_2))
                .check(
                        ViewAssertions.matches(
                                ViewMatchers.withText(
                                        Matchers.containsString(
                                                ERROR_MESSAGE_2
                                        )
                                )
                        )
                );
        intended(hasComponent(Profile6::class.java.name), Intents.times(0))
        Intents.release()

    }

    @Test
    fun correctInputFiresProfile6() {
        Intents.init()
        val chip1 = Espresso.onView(withId(R.id.chip1))
        val chip2 = Espresso.onView(withId(R.id.chip2))
        val chip3 = Espresso.onView(withId(R.id.chip3))
        chip1.perform(click())
        chip2.perform(click())
        chip3.perform(click())

        val buttonContinue = Espresso.onView(withId(R.id.button_p5))
        buttonContinue.perform(click())

        intended(hasComponent(Profile6::class.java.name))
        Intents.release()

    }


}