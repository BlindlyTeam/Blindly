package ch.epfl.sdp.blindly.settings

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.utils.UserHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


private const val TEST_SHOW_ME_MEN = "Men"
private const val TEST_SHOW_ME = "Women"
private const val TEST_RADIUS = "80km"

@HiltAndroidTest
class SettingsTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(Settings::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    lateinit var mainScenario: ActivityScenario<Settings>

    @Inject
    lateinit var user: UserHelper

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickingOnLocationButtonFiresSettingsLocationActivity() {
        init()
        onView(withId(R.id.location_button)).perform(click())
        var myLocation: TextView? = null
        activityRule.scenario.onActivity { activity ->
            myLocation = activity.findViewById(R.id.current_location_text)
        }
        intended(
            Matchers.allOf(
                hasComponent(SettingsLocation::class.java.name), IntentMatchers.hasExtra(
                    EXTRA_LOCATION,
                    myLocation?.text
                )
            )
        )
        release()
    }

    @Test
    fun clickingOnShowMeButtonFiresSettingsShowMeActivity() {
        init()
        onView(withId(R.id.show_me_button)).perform(click())
        var showMe: TextView ?= null
        activityRule.scenario.onActivity { activity ->
            showMe = activity.findViewById(R.id.show_me_text)
        }
        intended(
            Matchers.allOf(
                hasComponent(SettingsShowMe::class.java.name), IntentMatchers.hasExtra(
                    EXTRA_SHOW_ME,
                    showMe?.text
                )
            )
        )
        release()
    }

    @Test
    fun clickingOnDoneFiresBackToParentTheNewIntent() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), SettingsShowMe::class.java)
        intent.putExtra(EXTRA_SHOW_ME, TEST_SHOW_ME)
        ActivityScenario.launch<SettingsShowMe>(intent)

        onView((withId(R.id.show_me_men_button))).perform(click())
        onView(withId(R.id.done_button)).perform(click())

        assertEquals(RESULT_OK, activityRule.scenario.result.resultCode)
        assertEquals(
            TEST_SHOW_ME_MEN, activityRule.scenario.result.resultData.getStringExtra(
                EXTRA_SHOW_ME
            )
        )
    }

    private fun setProgress(progress: Int): ViewAction {
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
        radiusText.check(ViewAssertions.matches(ViewMatchers.withText(TEST_RADIUS)))
        release()
    }

    @Test
    fun checkEmailCorrect() {
        init()

        onView(withId(R.id.email_address_text)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(
                    user.getEmail()
                )
            )
        )
        release()
    }

    @Test
    fun checkEmailOpens() {
        init()
        onView(withId(R.id.email_button)).perform(click())

        intended(
                Matchers.allOf(
                        hasComponent(SettingsUpdateEmailTest::class.java.name)
                )
        )
        release()
    }

}