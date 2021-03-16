package ch.epfl.sdp.blindly

import android.view.View
import android.widget.SeekBar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.regex.Matcher


@RunWith(AndroidJUnit4::class)
class SettingsTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(Settings::class.java)

    @Test
    fun clickingOnLocationTextFiresSettingsLocationActivity() {
        init()
        onView(withId(R.id.current_location_text)).perform(click())
        intended(hasComponent(SettingsLocation::class.java.name))
        release()
    }

    @Test
    fun clickingOnShowMeTextFiresSettingsShowMeActivity() {
        init()
        onView(withId(R.id.show_me_text)).perform(click())
        intended(hasComponent(SettingsShowMe::class.java.name))
        release()
    }

    private fun setProgress(progress: Int): ViewAction? {
        return object : ViewAction {
            override fun perform(uiController: UiController?, view: View) {
                val seekBar = view as SeekBar
                seekBar.progress = progress
            }

            override fun getDescription(): String {
                return "Set a progress on a SeekBar"
            }

            override fun getConstraints(): org.hamcrest.Matcher<View>? {
                return ViewMatchers.isAssignableFrom(SeekBar::class.java)
            }
        }
    }

    @Test
    fun movingTheSeekBarChangesTheRadiusText() {
        init()
        onView(withId(R.id.seekBar)).perform(setProgress(80))
        val radiusText = onView(withId(R.id.radius_text))
        radiusText.check(ViewAssertions.matches(ViewMatchers.withText("80km")))
        release()
    }
}