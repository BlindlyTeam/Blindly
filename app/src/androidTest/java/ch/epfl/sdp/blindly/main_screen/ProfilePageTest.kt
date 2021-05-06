package ch.epfl.sdp.blindly.main_screen

import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.profile_edit.EditProfile
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.main_screen.fragments.ProfilePageFragment
import ch.epfl.sdp.blindly.settings.Settings
import ch.epfl.sdp.blindly.user.storage.UserCache
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.database.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
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

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var userCache: UserCache

    @Inject
    lateinit var db: FirebaseFirestore

    lateinit var fragment: Fragment

    @Before
    fun setup() {
        hiltRule.inject()
        goToProfileFragment()
        init()
    }

    @After
    fun afterEach() {
        release()
    }

    private fun goToProfileFragment() {
        activityRule.scenario.onActivity { act ->
            fragment = ProfilePageFragment.newInstance(2)

            act.supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment, "")
                .commitNow()
        }
    }

    @Test
    fun editButtonFiresEditProfileActivty() {
        onView(withId(R.id.edit_info_profile_button)).perform(click())
        intended(hasComponent(EditProfile::class.java.name))
    }

    @Test
    fun editButtonRemoveAudioPlayerFragment() {
        onView(withId(R.id.play_audio_profile_button)).perform(click())
        onView(withId(R.id.edit_info_profile_button)).perform(click())
        val audioPlayerFragment = getAudioPlayerFragment()
        assertThat(audioPlayerFragment, `is`(nullValue()))
    }

    @Test
    fun settingsButtonFiresSettingsActivity() {
        onView(withId(R.id.settings_profile_button)).perform(click())
        intended(hasComponent(Settings::class.java.name))
    }

    @Test
    fun settingsButtonRemovesAudioPlayerFragment() {
        onView(withId(R.id.play_audio_profile_button)).perform(click())
        onView(withId(R.id.settings_profile_button)).perform(click())
        val audioPlayerFragment = getAudioPlayerFragment()
        assertThat(audioPlayerFragment, `is`(nullValue()))
    }

    @Test
    fun playAudioButtonCreatesAudioPlayerFragment() {
        //Create and show the audio player
        onView(withId(R.id.play_audio_profile_button)).perform(click())
        val audioPlayerFragment = getAudioPlayerFragment()
        assertThat(audioPlayerFragment, `is`(notNullValue()))
        if (audioPlayerFragment != null) {
            assertThat(audioPlayerFragment.isVisible, `is`(true))
        }
    }

    @Test
    fun audioPlayerNotRemovedIfNotCreated() {
        val audioPlayerFragment = getAudioPlayerFragment()
        onView(withId(R.id.profile_relativeLayout)).perform(click())
        assertThat(audioPlayerFragment, `is`(nullValue()))
    }

    @Test
    fun clickingOutsideAudioPlayerFragmentRemovesIt() {
        //Create and show the audio player
        onView(withId(R.id.play_audio_profile_button)).perform(click())
        //Remove the audio player
        onView(withId(R.id.profile_relativeLayout)).perform(click())

        assertThat(getAudioPlayerFragment(), `is`(nullValue()))
    }

    @Test
    fun playAudioButtonCreateAudioPlayerFragment() {
        //Create and show the audio player
        onView(withId(R.id.play_audio_profile_button)).perform(click())
        //Remove the audio player
        onView(withId(R.id.profile_relativeLayout)).perform(click())
        //Create it again
        onView(withId(R.id.play_audio_profile_button)).perform(click())

        val audioPlayerFragment = getAudioPlayerFragment()
        assertThat(audioPlayerFragment, `is`(notNullValue()))
        if (audioPlayerFragment != null) {
            assertThat(audioPlayerFragment.isVisible, `is`(true))
        }
    }

    //TODO not yet implemented
    /*@Test
    fun recordButtonFiresRecordingActivity() {
        //Create and show the audio player
        onView(withId(R.id.play_audio_profile_button)).perform(click())

        onView(withId(R.id.record_button)).perform(click())
        intended(hasComponent(RecordingActivity::class.java.name))
    }

    @Test
    fun recordButtonRemovedAudioPlayerFragment() {
        //Create and show the audio player
        onView(withId(R.id.play_audio_profile_button)).perform(click())

        onView(withId(R.id.record_button)).perform(click())
        val fragmentManager = fragment.childFragmentManager
        val audioFragment = fragmentManager.findFragmentById(R.id.fragment_audio_container_view)
        assert(audioFragment == null)
    }

     */

    private fun getAudioPlayerFragment(): Fragment? {
        val fragmentManager = fragment.childFragmentManager
        return fragmentManager.findFragmentById(R.id.fragment_audio_container_view)
    }
}