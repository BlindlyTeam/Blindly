package ch.epfl.sdp.blindly

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.profile.Profile1
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test

class SignUpTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SignUp::class.java)

    @Test
    fun clickingOnCreateAccountLaunchProfile1() {
        init()
        Espresso.onView(withId(R.id.create_account_button)).perform(click())
        Intents.intended(Matchers.allOf(IntentMatchers.hasComponent(Profile1::class.java.name)))
        release()
    }
}