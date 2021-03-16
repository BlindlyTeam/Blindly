package ch.epfl.sdp.blindly

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.recording.RecordingActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfilePageTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainScreen::class.java)

    private fun gotoProfileScreen() {
        onView(withId(R.id.view_pager)).perform(swipeLeft())
        onView(withId(R.id.view_pager)).perform(swipeLeft())
        onView(withId(R.id.view_pager)).perform(swipeLeft())
    }
    @Test
    fun editButtonFiresEditProfileActivty() {
        init()
        gotoProfileScreen()
        onView(withId(R.id.edit_info_profile_button)).perform(click())
        intended(hasComponent(EditProfile::class.java.name))
        release()
    }

    @Test
    fun settingsButtonFiresSettingsActivity() {
        init()
        gotoProfileScreen()
        onView(withId(R.id.settings_profile_button)).perform(click())
        intended(hasComponent(Settings::class.java.name))
        release()
    }

    @Test
    fun recordAudioButtonFiresRecordingActivity() {
        init()
        gotoProfileScreen()
        onView(withId(R.id.record_audio_profile_button)).perform(click())
        intended(hasComponent(RecordingActivity::class.java.name))
        release()
    }

    @Test
    fun audioLibraryFiresAudioLibraryActivity() {
        init()
        gotoProfileScreen()
        onView(withId(R.id.audio_library_profile_button)).perform(click())
        intended(hasComponent(AudioLibrary::class.java.name))
        release()
    }

}