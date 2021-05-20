package ch.epfl.sdp.blindly.main_screen.match

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.main_screen.MainScreen
import ch.epfl.sdp.blindly.main_screen.match.cards.CardStackAdapter
import ch.epfl.sdp.blindly.main_screen.match.cards.Profile
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

private const val LOADING_MESSAGE = "Profiles are loading, please wait..."

@HiltAndroidTest
class TestMainToMatch {

    @Inject
    lateinit var storage: FirebaseStorage

    lateinit var fragment: MatchPageFragment

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
    fun cardStackAdapterIsCreatedOnFragmentCreation() {
        onView(withId(R.id.card_stack_view)).check(matches(isDisplayed()))
    }

    @Test
    fun launchingActivityDisplaysLoadingMessage() {
        onView(withId(R.id.no_profile_text)).check(matches(withText(LOADING_MESSAGE)))
    }

    @Test
    fun assertCardStackAdapterConstructorWorks() {
        activityRule.scenario.onActivity { act ->
            fragment = MatchPageFragment.newInstance(0)

            act.supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment, "")
                .commitNow()
            val profiles = createProfiles()
            val adapter = CardStackAdapter(profiles, storage, fragment.view!!)
            assertThat(adapter.itemCount, equalTo(profiles.size))
        }
    }

    private fun createProfiles(): List<Profile> {
        return listOf(
            Profile("UID1", "Jean", 21, "Man", 48, "PathJean"),
            Profile("UID2", "Pierre", 24, "Man", 37, "PathPierre"),
            Profile("UID3", "Jeanne", 22, "Woman", 42, "PathJeanne"),
            Profile("UID4", "Demi", 31, "NonBinary", 240, "PathDemi")
        )
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