package ch.epfl.sdp.blindly.main_screen

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.main_screen.audio_player.AudioPlayerFragment
import ch.epfl.sdp.blindly.recording.RecordingActivity
import ch.epfl.sdp.blindly.user.UserCache
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class AudioPlayerFragmentTest {

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

    @Before
    fun setup() {
        hiltRule.inject()
        createFragment()
        init()
    }

    @After
    fun afterEach() {
        release()
    }

    private fun createFragment() {
        activityRule.scenario.onActivity { act ->
            act.supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, AudioPlayerFragment.newInstance(), "")
                .commitNow()
        }
    }

    @Test
    fun recordFiresRecordingActivity() {
        onView(withId(R.id.record_button)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(RecordingActivity::class.java.name))
    }

}