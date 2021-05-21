package ch.epfl.sdp.blindly.main_screen.my_matches

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.viewpager2.widget.ViewPager2
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.main_screen.MainScreen
import ch.epfl.sdp.blindly.weather.WeatherActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@HiltAndroidTest
class MyMatchesFragmentTest {


    @get:Rule
    val activityRule = ActivityScenarioRule(MainScreen::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
        runBlocking { goToProfileFragment() }
        Intents.init()
    }

    @After
    fun afterEach() {
        Intents.release()
    }

    private suspend fun goToProfileFragment() {
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
    fun mapButtonFiresMapActivty() {
        onView(ViewMatchers.withId(R.id.buttonWeatherEvent)).check(
            ViewAssertions.matches(
                ViewMatchers.withEffectiveVisibility(
                    ViewMatchers.Visibility.VISIBLE
                )
            )
        ).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(WeatherActivity::class.java.name))
    }

}
