package ch.epfl.sdp.blindly.main_screen

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.EditProfile
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.recording.RecordingActivity
import ch.epfl.sdp.blindly.settings.Settings
import ch.epfl.sdp.blindly.user.UserHelper
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@HiltAndroidTest
class ProfilePageTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainScreen::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userHelper: UserHelper

    @Before
    fun setup() {
        hiltRule.inject()
    }

    private fun selectTabAtPosition(position: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return Matchers.allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))
            }

            override fun getDescription(): String {
                return "with tab at index$position"
            }

            override fun perform(uiController: UiController?, view: View) {
                if (view is TabLayout) {
                    val tabLayout = view as TabLayout
                    val tab = tabLayout.getTabAt(position)
                    tab?.select()
                }
            }
        }
    }

    @Test
    fun editButtonFiresEditProfileActivty() {
        init()
        onView(withId(R.id.tabs)).perform(selectTabAtPosition(2))
        onView(withId(R.id.edit_info_profile_button)).perform(click())
        intended(hasComponent(EditProfile::class.java.name))
        release()
    }

    @Test
    fun settingsButtonFiresSettingsActivity() {
        init()
        onView(withId(R.id.settings_profile_button)).perform(click())
        intended(hasComponent(Settings::class.java.name))
        release()
    }

    @Test
    fun recordAudioButtonFiresRecordingActivity() {
        init()
        onView(withId(R.id.record_audio_profile_button)).perform(click())
        intended(hasComponent(RecordingActivity::class.java.name))
        release()
    }

}