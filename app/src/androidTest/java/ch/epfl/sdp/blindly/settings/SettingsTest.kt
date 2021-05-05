package ch.epfl.sdp.blindly.settings

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.SplashScreen
import ch.epfl.sdp.blindly.user.UserHelper
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

private const val TEST_SHOW_ME_MEN = "Men"
private const val TEST_RADIUS = "80km"
private const val TEST_AGE_RANGE = "40 - 60"
private const val TEST_LOWER_AGE = 40
private const val TEST_HIGHER_AGE = 60
private const val TEST_RADIUS_VAL = 80

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
        init()
    }

    @After
    fun afterEach() {
        release()
    }

    @Test
    fun clickingOnLocationButtonFiresSettingsLocationActivity() {
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
    }

    @Test
    fun clickingOnShowMeButtonFiresSettingsShowMeActivity() {
        onView(withId(R.id.show_me_button)).perform(click())
        var showMe: TextView? = null
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
    }

    @Test
    fun clickingOnDoneFiresBackToParentTheNewIntent() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), SettingsShowMe::class.java)
        intent.putExtra(EXTRA_SHOW_ME, TEST_SHOW_ME_MEN)
        ActivityScenario.launch<SettingsShowMe>(intent)

        onView((withId(R.id.show_me_men_button))).perform(click())
        onView(withId(R.id.done_button)).perform(click())

        assertEquals(RESULT_OK, activityRule.scenario.result.resultCode)
        assertEquals(
            TEST_SHOW_ME_MEN,
            activityRule.scenario.result.resultData.getStringExtra(EXTRA_SHOW_ME)
        )
    }

    @Test
    fun movingTheSliderChangesTheRadiusText() {
        onView(withId(R.id.location_slider)).perform(setSliderValue(TEST_RADIUS_VAL))
        val radiusText = onView(withId(R.id.radius_text))
        radiusText.check(matches(withText(TEST_RADIUS)))
    }

    @Test
    fun movingTheRangeSliderChangesTheSelectedAgeRangeText() {
        onView(withId(R.id.age_range_slider)).perform(
            setRangeSliderValues(
                TEST_LOWER_AGE,
                TEST_HIGHER_AGE
            )
        )
        val selectedAgeRangeText = onView(withId(R.id.selected_age_range_text))
        selectedAgeRangeText.check(matches(withText(TEST_AGE_RANGE)))
    }

    @Test
    fun checkEmailCorrect() {
        onView(withId(R.id.email_address_text)).check(
            matches(
                withText(
                    user.getEmail()
                )
            )
        )
    }

    @Test
    fun checkEmailOpens() {
        onView(withId(R.id.email_button)).perform(click())
        intended(
            Matchers.allOf(
                hasComponent(SettingsUpdateEmail::class.java.name)
            )
        )
    }

    @Test
    fun clickingOnLogoutButtonFiresSplashScreen() {
        onView(withId(R.id.logout_button)).perform(click())
        Thread.sleep(1000)
        intended(
            hasComponent(SplashScreen::class.java.name)
        )
    }

    @Test
    fun clickingOnDeleteAccountButtonFiresSplashScreen() {
        onView(withId(R.id.delete_account_button)).perform(click())
        Thread.sleep(1000)
        intended(
            hasComponent(SplashScreen::class.java.name)
        )
    }

    private fun setSliderValue(value: Int): ViewAction {
        return object : ViewAction {
            override fun perform(uiController: UiController?, view: View) {
                val slider = view as Slider
                slider.value = value.toFloat()
            }

            override fun getDescription(): String {
                return "Set a value on a Slider"
            }

            override fun getConstraints(): org.hamcrest.Matcher<View>? {
                return isAssignableFrom(Slider::class.java)
            }
        }
    }

    private fun setRangeSliderValues(minValue: Int, maxValue: Int): ViewAction {
        return object : ViewAction {
            override fun perform(uiController: UiController?, view: View) {
                val rangeSlider = view as RangeSlider
                rangeSlider.values = listOf(minValue.toFloat(), maxValue.toFloat())
            }

            override fun getDescription(): String {
                return "Set the values on a RangeSlider"
            }

            override fun getConstraints(): org.hamcrest.Matcher<View>? {
                return isAssignableFrom(RangeSlider::class.java)
            }
        }
    }
}