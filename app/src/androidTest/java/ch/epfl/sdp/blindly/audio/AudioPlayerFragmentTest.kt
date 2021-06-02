package ch.epfl.sdp.blindly.audio

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.fake_module.FakeUserHelperModule.Companion.TEST_UID
import ch.epfl.sdp.blindly.main_screen.MainScreen
import ch.epfl.sdp.blindly.main_screen.profile.BUNDLE_UID
import ch.epfl.sdp.blindly.main_screen.profile.ProfilePageFragment
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.storage.UserCache
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

    lateinit var fragment: Fragment

    @Before
    fun setup() {
        hiltRule.inject()
        goToFragment()
        Intents.init()
    }

    @After
    fun afterEach() {
        Intents.release()
    }

    private fun goToFragment() {
        activityRule.scenario.onActivity { act ->
            fragment = AudioPlayerFragment.newInstance(TEST_UID)

            act.supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment, "")
                .commitNow()
        }
    }

    @Test
    fun clickOnPlay() {
        onView(withId(R.id.play_pause_button)).perform(click())
    }

    @Test
    fun recordFiresRecordignActivity() {
        onView(withId(R.id.record_button)).perform(click())
        intended(hasComponent(RecordingActivity::class.java.name))
    }
}