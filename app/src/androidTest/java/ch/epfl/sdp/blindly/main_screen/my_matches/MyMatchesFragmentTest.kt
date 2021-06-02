package ch.epfl.sdp.blindly.main_screen.my_matches

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import androidx.viewpager2.widget.ViewPager2
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.main_screen.ANSWER_NO
import ch.epfl.sdp.blindly.main_screen.ANSWER_YES
import ch.epfl.sdp.blindly.main_screen.MainScreen
import ch.epfl.sdp.blindly.main_screen.map.UserMapActivity
import ch.epfl.sdp.blindly.main_screen.my_matches.MyMatchesAdapter.Companion.REMOVE_USER_WARNING_MESSAGE
import ch.epfl.sdp.blindly.main_screen.my_matches.MyMatchesAdapter.Companion.REMOVE_USER_WARNING_TITLE
import ch.epfl.sdp.blindly.main_screen.my_matches.chat.ChatActivity
import ch.epfl.sdp.blindly.main_screen.my_matches.match_profile.MatchProfileActivity
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.storage.UserCache
import ch.epfl.sdp.blindly.weather.WeatherActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltAndroidTest
class MyMatchesFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainScreen::class.java)

    @get:Rule
    var permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userHelper: UserHelper

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var userCache: UserCache

    @Before
    fun setup() {
        hiltRule.inject()
        runBlocking { goToMyMatchesFragment() }
        init()
    }

    @After
    fun afterEach() {
        release()
    }

    private suspend fun goToMyMatchesFragment() {
        // Wait for everything to settle to avoid race conditions
        // Waiting synchronously isn't a problem since there are no other
        // tasks running and we don't care about UI responsivity while testing
        suspendCoroutine { cont: Continuation<Boolean> ->
            activityRule.scenario.onActivity { act ->
                act.viewPager!!.currentItem = 1
                // After setting the item, wait for it to be fully shown
                act.viewPager!!.registerOnPageChangeCallback(object :
                    ViewPager2.OnPageChangeCallback() {
                    override fun onPageScrollStateChanged(state: Int) {
                        if (state == ViewPager2.SCROLL_STATE_IDLE)
                            cont.resume(true);
                    }
                })
            }
        }
    }

    @Test
    fun weatherButtonFiresWeatherActivty() {
        onView(withId(R.id.buttonWeatherEvent)).check(
            matches(
                withEffectiveVisibility(
                    Visibility.VISIBLE
                )
            )
        ).perform(click())
        intended(hasComponent(WeatherActivity::class.java.name))
    }

    @Test
    fun chatButtonFiresChatActivity() {
        onView(withId(R.id.userNameLayout)).perform(click())
        onView(withId(R.id.chatButton)).perform(click())
        intended(hasComponent(ChatActivity::class.java.name))
    }

    @Test
    fun profileButtonFiresMatchProfileActivity() {
        onView(withId(R.id.userNameLayout)).perform(click())
        onView(withId(R.id.profileButton)).perform(click())
        intended(hasComponent(MatchProfileActivity::class.java.name))
    }

    @Test
    fun mapButtonFiresUserMapActivity() {
        onView(withId(R.id.userNameLayout)).perform(click())
        onView(withId(R.id.mapButton)).perform(click())
        intended(hasComponent(UserMapActivity::class.java.name))
    }

    @Test
    fun removeButtonRemovesPromptsUser() {
        onView(withId(R.id.removeMatchButton)).perform(click())
        onView(withText(REMOVE_USER_WARNING_TITLE)).check(matches(isDisplayed()))
        onView(withText(REMOVE_USER_WARNING_MESSAGE)).check(matches(isDisplayed()))
        onView(withText(ANSWER_YES)).inRoot(isDialog()).check(matches(isDisplayed()))
        onView(withText(ANSWER_NO)).inRoot(isDialog()).check(matches(isDisplayed()))
    }
}
