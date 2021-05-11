package ch.epfl.sdp.blindly.main_screen.profile.settings

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
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
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.fake_module.FakeUserCacheModule.Companion.fakeUser
import ch.epfl.sdp.blindly.fake_module.FakeUserCacheModule.Companion.fakeUserUpdated
import ch.epfl.sdp.blindly.main_screen.profile.settings.*
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.storage.UserCache
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

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

    @Inject
    lateinit var userHelper: UserHelper

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var userCache: UserCache

    @Inject
    lateinit var db: FirebaseFirestore


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
        var location: TextView? = null
        activityRule.scenario.onActivity { activity ->
            location = activity.findViewById(R.id.current_location_text)
        }
        intended(
            Matchers.allOf(
                hasComponent(SettingsLocation::class.java.name), IntentMatchers.hasExtra(
                    EXTRA_LOCATION,
                    location?.text
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
                    userHelper.getEmail()
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

    @Test
    fun locationIsCorrectlyRetrieved() {
        val TEST_LOCATION = "Ecublens, Switzerland"
        val location = onView(withId(R.id.current_location_text))
        location.check(matches(withText(TEST_LOCATION)))
    }

    @Test
    fun radiusIsCorrectlyRetrieved() {
        val TEST_RADIUS = fakeUser.radius
        var radiusSlider = 0
        activityRule.scenario.onActivity {
            radiusSlider = it.findViewById<Slider>(R.id.location_slider).value.toInt()
        }
        assertThat(radiusSlider, equalTo(TEST_RADIUS))
    }

    @Test
    fun showMeIsCorrectlyRetrieved() {
        val TEST_SHOW_ME = fakeUser.showMe
        val showMe = onView(withId(R.id.show_me_text))
        showMe.check(matches(withText(TEST_SHOW_ME)))
    }

    @Test
    fun ageRangeIsCorrectlyRetrieved() {
        val TEST_AGE_RANGE = fakeUser.ageRange
        var ageRangeSlider: List<Float> = listOf()
        activityRule.scenario.onActivity {
            ageRangeSlider = it.findViewById<RangeSlider>(R.id.age_range_slider).values
        }
        assertThat(ageRangeSlider[0].toInt(), equalTo(TEST_AGE_RANGE?.get(0)))
        assertThat(ageRangeSlider[1].toInt(), equalTo(TEST_AGE_RANGE?.get(1)))
    }

    @Test
    fun onBackPressedDoesNotUpdateIfRadiusIsTheSame() {
        val TEST_RADIUS = fakeUser.radius
        var radiusSlider = 0

        activityRule.scenario.onActivity {
            radiusSlider = it.findViewById<Slider>(R.id.location_slider).value.toInt()
        }
        assertThat(radiusSlider, equalTo(TEST_RADIUS))
        //Press back button
        val activity = pressBackAndRelaunchSettings()
        activity.onActivity {
            radiusSlider = it.findViewById<Slider>(R.id.location_slider).value.toInt()
        }
        assertThat(radiusSlider, equalTo(TEST_RADIUS))
    }

    @Test
    fun onBackPressedUpdatesRadius() {
        var radiusSlider = 0
        activityRule.scenario.onActivity {
            radiusSlider = it.findViewById<Slider>(R.id.location_slider).value.toInt()
        }
        assertThat(radiusSlider, equalTo(fakeUser.radius))
        //Update radius
        val TEST_RADIUS = fakeUserUpdated.radius
        onView(withId(R.id.location_slider)).perform(TEST_RADIUS?.let { setSliderValue(it) })
        //Press back button
        val activity = pressBackAndRelaunchSettings()
        activity.onActivity {
            radiusSlider = it.findViewById<Slider>(R.id.location_slider).value.toInt()
        }
        assertThat(radiusSlider, equalTo(TEST_RADIUS))
    }

    @Test
    fun onBackPressedDoesNotUpdateIfAgeRangeIsTheSame() {
        val TEST_AGE_RANGE = getFakeUserAgeRange()

        var ageRangeSlider = listOf<Float>()
        activityRule.scenario.onActivity {
            ageRangeSlider = it.findViewById<RangeSlider>(R.id.age_range_slider).values
        }
        assertThat(ageRangeSlider, equalTo(TEST_AGE_RANGE))
        //Press back button
        val activity = pressBackAndRelaunchSettings()
        activity.onActivity {
            ageRangeSlider = it.findViewById<RangeSlider>(R.id.age_range_slider).values
        }
        assertThat(ageRangeSlider, equalTo(TEST_AGE_RANGE))
    }

    @Test
    fun onBackPressedUpdatesAgeRange() {
        val TEST_AGE_RANGE_USER = getFakeUserAgeRange()

        var ageRangeSlider = listOf<Float>()
        activityRule.scenario.onActivity {
            ageRangeSlider = it.findViewById<RangeSlider>(R.id.age_range_slider).values
        }
        assertThat(ageRangeSlider, equalTo(TEST_AGE_RANGE_USER))
        //Update radius
        val TEST_AGE_RANGE = getFakeUser2AgeRange()
        onView(withId(R.id.age_range_slider)).perform(
            TEST_AGE_RANGE?.get(0)?.let {
                setRangeSliderValues(
                    it.toInt(),
                    TEST_AGE_RANGE[1].toInt()
                )
            }
        )
        //Press back button
        val activity = pressBackAndRelaunchSettings()
        activity.onActivity {
            ageRangeSlider = it.findViewById<RangeSlider>(R.id.age_range_slider).values
        }
        assertThat(ageRangeSlider, equalTo(TEST_AGE_RANGE))
    }

    private fun pressBackAndRelaunchSettings(): ActivityScenario<Settings> {
        Espresso.pressBackUnconditionally()
        val intent = Intent(ApplicationProvider.getApplicationContext(), Settings::class.java)
        return ActivityScenario.launch(intent)
    }

    private fun getFakeUserAgeRange(): List<Float>? {
        val userAgeRange = fakeUser.ageRange

        return userAgeRange?.get(0)?.let { listOf(it.toFloat(), userAgeRange[1].toFloat()) }
    }

    private fun getFakeUser2AgeRange(): List<Float>? {
        val userAgeRange2 = fakeUserUpdated.ageRange

        return userAgeRange2?.get(0)?.let { listOf(it.toFloat(), userAgeRange2[1].toFloat()) }
    }
}