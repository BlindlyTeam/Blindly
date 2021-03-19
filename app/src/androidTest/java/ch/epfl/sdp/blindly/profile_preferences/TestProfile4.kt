package ch.epfl.sdp.blindly.profile_preferences

import androidx.test.espresso.Espresso
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
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.profile.Profile4
import ch.epfl.sdp.blindly.profile.Profile4_2
import ch.epfl.sdp.blindly.profile.Profile5
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TestProfile4 {


    @get:Rule
    val activityRule = ActivityScenarioRule(Profile4::class.java)

    private val NO_INPUT_ERROR = "Please select one!"

    @Test
    fun noInputOutputsError() {
        Intents.init()
        val buttonContinue = Espresso.onView(withId(R.id.button_p4))
        buttonContinue.perform(click())
        intended(hasComponent(Profile5::class.java.name), times(0))
        intended(hasComponent(Profile4_2::class.java.name), times(0))
        Espresso.onView(withId(R.id.warning_p4))
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
    fun womanFiresProfile5() {
        Intents.init()
        val buttonWoman = Espresso.onView(withId(R.id.sex1_user))
        buttonWoman.perform(click())
        val buttonContinue = Espresso.onView(withId(R.id.button_p4))
        buttonContinue.perform(click())
        intended(hasComponent(Profile5::class.java.name))
        Intents.release()
    }

    @Test
    fun manFiresProfile5() {
        Intents.init()
        val buttonWoman = Espresso.onView(withId(R.id.sex2_user))
        buttonWoman.perform(click())
        val buttonContinue = Espresso.onView(withId(R.id.button_p4))
        buttonContinue.perform(click())
        intended(hasComponent(Profile5::class.java.name))
        Intents.release()
    }

    @Test
    fun moreFiresProfile4_2() {
        Intents.init()
        val buttonMore = Espresso.onView(withId(R.id.sex3_more))
        buttonMore.perform(click())
        val buttonContinue = Espresso.onView(withId(R.id.button_p4))
        buttonContinue.perform(click())
        intended(hasComponent(Profile4_2::class.java.name))
        Intents.release()
    }


}