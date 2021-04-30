package ch.epfl.sdp.blindly.main_screen

import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.EditProfile
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.main_screen.profile.ProfilePageFragment
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
            val fragment: Fragment = ProfilePageFragment.newInstance(2)

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
    fun settingsButtonFiresSettingsActivity() {
        onView(withId(R.id.settings_profile_button)).perform(click())
        intended(hasComponent(Settings::class.java.name))
    }

}