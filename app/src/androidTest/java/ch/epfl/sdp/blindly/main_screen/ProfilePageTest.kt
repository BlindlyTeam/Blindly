package ch.epfl.sdp.blindly.main_screen

import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.EditProfile
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.main_screen.profile.ProfilePageFragment
import ch.epfl.sdp.blindly.matchers.EspressoTestMatchers
import ch.epfl.sdp.blindly.recording.RecordingActivity
import ch.epfl.sdp.blindly.settings.Settings
import ch.epfl.sdp.blindly.user.UserCache
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
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
        assert(audioPlayerFragment == null)
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
        assert(audioPlayerFragment == null)
    }

    @Test
    fun playAudioButtonCreatesAudioPlayerFragment() {
        //Create and show the audio player
        onView(withId(R.id.play_audio_profile_button)).perform(click())
        val audioPlayerFragment = getAudioPlayerFragment()
        assert(audioPlayerFragment != null)
        if (audioPlayerFragment != null) {
            assert(audioPlayerFragment.isVisible)
        }
    }

    @Test
    fun audioPlayerNotRemovedIfNotCreated() {
        val audioPlayerFragment = getAudioPlayerFragment()
        onView(withId(R.id.profile_relativeLayout)).perform(click())
        assert(audioPlayerFragment == null)
    }

    @Test
    fun removingAudioPlayerRestartsMediaPlayer() {
        //Create and show the audio player
        onView(withId(R.id.play_audio_profile_button)).perform(click())
        onView(withId(R.id.play_pause_button)).perform(click())
        //Remove the audio player
        onView(withId(R.id.profile_relativeLayout)).perform(click())
        //Create it again
        onView(withId(R.id.play_audio_profile_button)).perform(click())

        var playBar: SeekBar? = null
        activityRule.scenario.onActivity {
            playBar = it.findViewById(R.id.play_bar)
        }
        assertThat(playBar?.progress, equalTo(0))
    }

    @Test
    fun clickingOutsideAudioPlayerFragmentRemovesIt() {
        //Create and show the audio player
        onView(withId(R.id.play_audio_profile_button)).perform(click())
        //Remove the audio player
        onView(withId(R.id.profile_relativeLayout)).perform(click())

        assert(getAudioPlayerFragment() == null)
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
        assert(audioPlayerFragment != null)
        if (audioPlayerFragment != null) {
            assert(audioPlayerFragment.isVisible)
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

    @Test
    fun playPauseButtonChangesBackgroundWhenClickedTwice() {
        //Create and show the audio player
        onView(withId(R.id.play_audio_profile_button)).perform(click())

        onView(withId(R.id.play_pause_button))
            .perform(click())
        onView(withId(R.id.play_pause_button))
            .perform(click())
        onView(withId(R.id.play_pause_button))
            .check(
                ViewAssertions.matches(
                    EspressoTestMatchers.withDrawable(android.R.drawable.ic_media_play)
                )
            )
    }

    @Test
    fun playPauseButtonChangesBackgroundWhenPlayIsFinished() {
        //Create and show the audio player
        onView(withId(R.id.play_audio_profile_button)).perform(click())

        onView(withId(R.id.play_pause_button))
            .perform(click())
        Thread.sleep(2000L)
        onView(withId(R.id.play_pause_button))
            .check(
                ViewAssertions.matches(
                    EspressoTestMatchers.withDrawable(android.R.drawable.ic_media_play)
                )
            )
    }


    private fun getAudioPlayerFragment(): Fragment? {
        val fragmentManager = fragment.childFragmentManager
        return fragmentManager.findFragmentById(R.id.fragment_audio_container_view)
    }

}