package ch.epfl.sdp.blindly.profile_preferences

import androidx.test.espresso.Espresso.closeSoftKeyboard
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
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.profile.ProfileBirthday
import ch.epfl.sdp.blindly.profile.ProfileName
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TestProfileName {


    @get:Rule
    val activityRule = ActivityScenarioRule(ProfileName::class.java)

    private val CORRECT_NAME = "Alice"
    private val INCORRECT_CHARS = "Ali;;;ce"
    private val INCORRECT_SHORT_NAME = "A"
    private val INCORRECT_LONG_NAME = "abcdefabcdefabcdefabcdef"
    private val ERROR_LONG_NAME = "Name can't be more than 20 characters!"
    private val ERROR_SHORT_NAME = "Name can't be less than 2 characters!"
    private val ERROR_CHARACTERS = "Please use only letters."

    @Test
    fun testProfile2FiresProfile3() {
        Intents.init()
        onView(withId(R.id.text_first_name))
                .perform(ViewActions.clearText(), ViewActions.typeText(CORRECT_NAME));
        closeSoftKeyboard();
        val buttonContinue = onView(withId(R.id.button_p2))
        buttonContinue.perform(click())
        intended(hasComponent(ProfileBirthday::class.java.name))
        Intents.release()
    }

    @Test
    fun shortNameOutputsError() {
        Intents.init()
        onView(withId(R.id.text_first_name))
                .perform(ViewActions.clearText(), ViewActions.typeText(INCORRECT_SHORT_NAME));
        closeSoftKeyboard();
        val buttonContinue = onView(withId(R.id.button_p2))
        buttonContinue.perform(click())
        onView(withId(R.id.warning1_p2)).check(
                ViewAssertions.matches(
                        ViewMatchers.withText(
                                Matchers.containsString(
                                        ERROR_SHORT_NAME
                                )
                        )
                )
        );
        intended(hasComponent(ProfileBirthday::class.java.name), times(0))
        Intents.release()
    }

    @Test
    fun longNameOutputsError() {
        Intents.init()
        onView(withId(R.id.text_first_name))
                .perform(ViewActions.clearText(), ViewActions.typeText(INCORRECT_LONG_NAME));
        closeSoftKeyboard();
        val buttonContinue = onView(withId(R.id.button_p2))
        buttonContinue.perform(click())
        onView(withId(R.id.warning2_p2)).check(
                ViewAssertions.matches(
                        ViewMatchers.withText(
                                Matchers.containsString(
                                        ERROR_LONG_NAME
                                )
                        )
                )
        );
        intended(hasComponent(ProfileBirthday::class.java.name), times(0))
        Intents.release()
    }

    @Test
    fun incorrectCharsOutputError() {
        Intents.init()
        onView(withId(R.id.text_first_name))
                .perform(ViewActions.clearText(), ViewActions.typeText(INCORRECT_CHARS));
        closeSoftKeyboard();
        val buttonContinue = onView(withId(R.id.button_p2))
        buttonContinue.perform(click())
        onView(withId(R.id.warning3_p2)).check(
                ViewAssertions.matches(
                        ViewMatchers.withText(
                                Matchers.containsString(
                                        ERROR_CHARACTERS
                                )
                        )
                )
        );
        intended(hasComponent(ProfileBirthday::class.java.name), times(0))
        Intents.release()
    }


}