package ch.epfl.sdp.blindly

import android.view.View
import android.widget.SeekBar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.settings.Settings
import ch.epfl.sdp.blindly.settings.SettingsLocation
import ch.epfl.sdp.blindly.settings.SettingsShowMe
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


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
    fun clickingOnLocationButtonFiresSettingsLocationActivity() {
        init()
        onView(withId(R.id.modify_location_button)).perform(click())
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

    @Test
    fun clickingOnShowMeButtonFiresSettingsShowMeActivity() {
        init()
        onView(withId(R.id.modify_genre_button)).perform(click())
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

    /*TODO: need to check that the value modified by the children (SettingsLocation and
            SeetingsShowMe) propagate to the parent activity
     */
}