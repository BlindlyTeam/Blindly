package ch.epfl.sdp.blindly.match

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.MainScreen
import ch.epfl.sdp.blindly.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class TestMainToMatch {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainScreen::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun onButtonClickLunchMatchActivity() {
        init()
        Espresso.onView(withId(R.id.view_pager)).perform(swipeLeft())
        Espresso.onView(withId(R.id.button_to_match)).perform(click())
        intended(hasComponent(MatchActivity::class.java.name))
        release()
    }
}