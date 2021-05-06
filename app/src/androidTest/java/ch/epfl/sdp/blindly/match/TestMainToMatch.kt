package ch.epfl.sdp.blindly.match

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.main_screen.MainScreen
import ch.epfl.sdp.blindly.match.cards.Profile
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

const val SLEEP_TIME = 250L

@HiltAndroidTest
class TestMainToMatch {
    private val profiles = createProfiles()

    @get:Rule
    val activityRule = ActivityScenarioRule(MainScreen::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
        init()
    }

    @After
    fun afterEach() {
        release()
    }

    @Test
    fun firstCardIsWellDisplayed() {
        onView(withId(R.id.card_stack_view)).check(
            ViewAssertions.matches(
                ViewMatchers.hasDescendant(
                    ViewMatchers.withText(profiles[0].name)
                )
            )
        )
        onView(withId(R.id.card_stack_view)).check(
            ViewAssertions.matches(
                ViewMatchers.hasDescendant(
                    ViewMatchers.withText(profiles[0].age.toString())
                )
            )
        )
    }

    @Test
    fun skipButtonDisplaysSecondCard() {
        onView(withId(R.id.skip_button)).perform(click())
        Thread.sleep(SLEEP_TIME)
        onView(withId(R.id.card_stack_view)).check(
            ViewAssertions.matches(
                ViewMatchers.hasDescendant(
                    ViewMatchers.withText(profiles[1].name)
                )
            )
        )
        onView(withId(R.id.card_stack_view)).check(
            ViewAssertions.matches(
                ViewMatchers.hasDescendant(
                    ViewMatchers.withText(profiles[1].age.toString())
                )
            )
        )
    }

    @Test
    fun likeButtonDisplaysSecondCard() {
        onView(withId(R.id.like_button)).perform(click())
        Thread.sleep(SLEEP_TIME)
        onView(withId(R.id.card_stack_view)).check(
            ViewAssertions.matches(
                ViewMatchers.hasDescendant(
                    ViewMatchers.withText(profiles[1].name)
                )
            )
        )
        onView(withId(R.id.card_stack_view)).check(
            ViewAssertions.matches(
                ViewMatchers.hasDescendant(
                    ViewMatchers.withText(profiles[1].age.toString())
                )
            )
        )
    }

    @Test
    fun rewindButtonHasNoEffectOnFirstCard() {
        onView(withId(R.id.rewind_button)).perform(click())
        Thread.sleep(SLEEP_TIME)
        onView(withId(R.id.card_stack_view)).check(
            ViewAssertions.matches(
                ViewMatchers.hasDescendant(
                    ViewMatchers.withText(profiles[0].name)
                )
            )
        )
        onView(withId(R.id.card_stack_view)).check(
            ViewAssertions.matches(
                ViewMatchers.hasDescendant(
                    ViewMatchers.withText(profiles[0].age.toString())
                )
            )
        )
    }

    @Test
    fun rewindOnSecondCardReturnsToFirstCard() {
        onView(withId(R.id.like_button)).perform(click())
        Thread.sleep(SLEEP_TIME)
        onView(withId(R.id.rewind_button)).perform(click())
        Thread.sleep(SLEEP_TIME)
        onView(withId(R.id.card_stack_view)).check(
            ViewAssertions.matches(
                ViewMatchers.hasDescendant(
                    ViewMatchers.withText(profiles[0].name)
                )
            )
        )
        onView(withId(R.id.card_stack_view)).check(
            ViewAssertions.matches(
                ViewMatchers.hasDescendant(
                    ViewMatchers.withText(profiles[0].age.toString())
                )
            )
        )
    }

    @Test
    fun swipeWorksAsButtons() {
        onView(withId(R.id.card_stack_view)).perform(ViewActions.swipeRight())
        Thread.sleep(SLEEP_TIME)
        onView(withId(R.id.card_stack_view)).check(
            ViewAssertions.matches(
                ViewMatchers.hasDescendant(
                    ViewMatchers.withText(profiles[1].name)
                )
            )
        )
        onView(withId(R.id.card_stack_view)).check(
            ViewAssertions.matches(
                ViewMatchers.hasDescendant(
                    ViewMatchers.withText(profiles[1].age.toString())
                )
            )
        )
    }

    private fun createProfiles(): List<Profile> {
        val profiles = ArrayList<Profile>()
        profiles.add(Profile("Michelle", 25))
        profiles.add(Profile("Jean", 32))
        profiles.add(Profile("Jacques", 28))
        profiles.add(Profile("Bernadette", 35))
        profiles.add(Profile("Jeannine", 46))
        profiles.add(Profile("Kilian", 25))
        profiles.add(Profile("Melissa", 20))
        profiles.add(Profile("Tibor", 36))
        profiles.add(Profile("Cagin", 27))
        profiles.add(Profile("Capucine", 21))
        return profiles
    }
}