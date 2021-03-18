package ch.epfl.sdp.blindly


import androidx.test.espresso.intent.Intents
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class MapsTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LocationPermissionActivity::class.java)


    @Test
    fun testMap() {
        Intents.init()
        val buttonStart = Espresso.onView(withId(R.id.button))
        buttonStart.perform(click())
        intended(hasComponent(MapsActivity::class.java.name))
        Intents.release()
    }
} 