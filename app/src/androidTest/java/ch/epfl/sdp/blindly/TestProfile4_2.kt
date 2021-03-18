package ch.epfl.sdp.blindly

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.times
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TestProfile4_2 {
    private val TEST_NAME = "Alice"

    @get:Rule
    val activityRule = ActivityScenarioRule(Profile4_2::class.java)


    @Test
    fun testProfile4_2DoesNotFireProfile5WithoutText() {
        Intents.init()
        val buttonStart = Espresso.onView(withId(R.id.button_p4_2))
        buttonStart.perform(click())
        intended(hasComponent(Profile5::class.java.name), times(0))
        Intents.release()
    }

//    @Test
//    fun testProfile4_2FireProfile5() {
//        Intents.init()
//        onView(withId(R.id.text_p4_2)).perform(clearText(), typeText(TEST_NAME));
//        closeSoftKeyboard();
//        val buttonStart = Espresso.onView(withId(R.id.button_p4_2))
//        buttonStart.perform(click())
//        intended(hasComponent(Profile5::class.java.name))
//        Intents.release()
//    }

}