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
import ch.epfl.sdp.blindly.MainActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.profile.Profile7
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TestProfile7 {


    @get:Rule
    val activityRule = ActivityScenarioRule(Profile7::class.java)

    private val ERROR_MESSAGE_1 = "Please select at least one!"
    private val ERROR_MESSAGE_2 = "You can not select more than 5!"

    /*
    TODO
    As the linking between profile preferences and the other activities are not done
    yet, Profile7 for now sends back to MainActivity.
    */
    @Test
    fun noInputShowsError() {
        Intents.init()
        val buttonContinue = Espresso.onView(withId(R.id.button_p7))
        buttonContinue.perform(click())

        Espresso.onView(withId(R.id.warning_p7_1))
                .check(
                        ViewAssertions.matches(
                                ViewMatchers.withText(
                                        Matchers.containsString(
                                                ERROR_MESSAGE_1
                                        )
                                )
                        )
                );
        intended(hasComponent(MainActivity::class.java.name), Intents.times(0))
        Intents.release()
    }

    /*
    TODO
    As the linking between profile preferences and the other activities are not done
    yet, Profile7 for now sends back to MainActivity.
    */
    @Test
    fun moreThanAllowedInputShowsError() {
        Intents.init()

        val chip14 = Espresso.onView(withId(R.id.chip14))
        val chip16 = Espresso.onView(withId(R.id.chip16))
        val chip17 = Espresso.onView(withId(R.id.chip17))
        val chip19 = Espresso.onView(withId(R.id.chip19))
        val chip20 = Espresso.onView(withId(R.id.chip20))
        val chip23 = Espresso.onView(withId(R.id.chip23))


        chip14.perform(click())
        chip16.perform(click())
        chip17.perform(click())
        chip19.perform(click())
        chip20.perform(click())
        chip23.perform(click())

        val buttonContinue = Espresso.onView(withId(R.id.button_p7))
        buttonContinue.perform(click())

        Espresso.onView(withId(R.id.warning_p7_2))
                .check(
                        ViewAssertions.matches(
                                ViewMatchers.withText(
                                        Matchers.containsString(
                                                ERROR_MESSAGE_2
                                        )
                                )
                        )
                );
        intended(hasComponent(MainActivity::class.java.name), Intents.times(0))
        Intents.release()

    }

    /*
    TODO
    As the linking between profile preferences and the other activities are not done
    yet, Profile7 for now sends back to MainActivity.
     */
    @Test
    fun correctInputsFireMainActivity() {
        Intents.init()
        val chip18 = Espresso.onView(withId(R.id.chip18))
        val chip28 = Espresso.onView(withId(R.id.chip28))
        val chip38 = Espresso.onView(withId(R.id.chip38))

        chip18.perform(click())
        chip28.perform(click())
        chip38.perform(click())

        val buttonContinue = Espresso.onView(withId(R.id.button_p7))
        buttonContinue.perform(click())

        intended(hasComponent(MainActivity::class.java.name))
        Intents.release()

    }


}