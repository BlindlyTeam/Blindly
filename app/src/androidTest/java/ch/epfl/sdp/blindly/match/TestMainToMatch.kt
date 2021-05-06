package ch.epfl.sdp.blindly.match

import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.release
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.main_screen.MainScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule

const val SLEEP_TIME = 250L

@HiltAndroidTest
class TestMainToMatch {
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

    //Had to remove the tests while we look for a way to test the DB input
    /*@Test
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
    }*/
}