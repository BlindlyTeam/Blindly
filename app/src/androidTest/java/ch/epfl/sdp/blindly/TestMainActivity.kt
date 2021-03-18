package ch.epfl.sdp.blindly

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TestMainActivity {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)


    @Test
    fun testMainActivity() {
        Intents.init()
        val buttonStart = Espresso.onView(withId(R.id.button_start))
        buttonStart.perform(click())
        intended(hasComponent(Profile1::class.java.name))
        Intents.release()
    }

    @Test
    fun testMainScreenButton() {
        Intents.init()
        Espresso.onView(withId(R.id.button)).perform(click())
        intended(hasComponent(MainScreen::class.java.name))
        Intents.release()
    }

}