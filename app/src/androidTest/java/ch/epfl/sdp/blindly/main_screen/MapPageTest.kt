package ch.epfl.sdp.blindly.main_screen

import ch.epfl.sdp.blindly.UserMapActivity

import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.EditProfile
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.main_screen.profile.ProfilePageFragment
import ch.epfl.sdp.blindly.recording.RecordingActivity
import ch.epfl.sdp.blindly.settings.Settings
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
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class MapPageTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainScreen::class.java)

    @Before
    fun setup() {
        goToProfileFragment()
        init()
    }

    @After
    fun afterEach() {
        release()
    }

    private fun goToProfileFragment() {
        activityRule.scenario.onActivity { act ->
            val fragment: Fragment = ProfilePageFragment.newInstance(3)

            act.supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment, "")
                .commitNow()
        }
    }

    @Test
    fun mapButtonFiresEditProfileActivty() {
        onView(withId(R.id.button_to_match)).perform(click())
        intended(hasComponent(UserMapActivity::class.java.name))
    }



}