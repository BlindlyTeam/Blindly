package ch.epfl.sdp.blindly.main_screen

import ch.epfl.sdp.blindly.UserMapActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.viewpager2.widget.ViewPager2
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
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltAndroidTest
class MapPageTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainScreen::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
        runBlocking { goToProfileFragment() }
        init()
    }

    @After
    fun afterEach() {
        release()
    }

    private suspend fun goToProfileFragment() {
        // Wait for everything to settle to avoid race conditions
        // Waiting synchronously isn't a problem since there are no other
        // tasks running and we don't care about UI responsivity while testing
        suspendCoroutine { cont: Continuation<Boolean> ->
            activityRule.scenario.onActivity { act ->
                act.viewPager!!.currentItem = 3
                // After setting the item, wait for it to be fully shown
                act.viewPager!!.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                    override fun onPageScrollStateChanged(state: Int) {
                        if (state == ViewPager2.SCROLL_STATE_IDLE)
                            cont.resume(true);
                    }
                })
            }
        }
    }

    @Test
    fun mapButtonFiresMapActivty() {
        onView(withId(R.id.open_map)).check(
            ViewAssertions.matches(
                ViewMatchers.withEffectiveVisibility(
                    ViewMatchers.Visibility.VISIBLE
                )
            )
        ).perform(click())

        intended(hasComponent(UserMapActivity::class.java.name))
    }
}