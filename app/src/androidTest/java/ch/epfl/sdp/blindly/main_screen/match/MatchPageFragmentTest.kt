package ch.epfl.sdp.blindly.main_screen.match

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.audio.FirebaseRecordings
import ch.epfl.sdp.blindly.main_screen.MainScreen
import ch.epfl.sdp.blindly.main_screen.match.cards.CardStackAdapter
import ch.epfl.sdp.blindly.main_screen.match.cards.Profile
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

private const val LOADING_MESSAGE = "Profiles are loading, please wait…"

@HiltAndroidTest
class MatchPageFragmentTest {

    @Inject
    lateinit var recordings: FirebaseRecordings

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
            val profiles = listOf(
                Profile("UID1", "Jean", 21, "Man", 48, "PathJean"),
                Profile("UID2", "Pierre", 24, "Man", 37, "PathPierre"),
                Profile("UID3", "Jeanne", 22, "Woman", 42, "PathJeanne"),
                Profile("UID4", "Demi", 31, "NonBinary", 240, "PathDemi")
            )
            val adapter = CardStackAdapter(profiles, recordings, fragment.view!!)
            assertThat(adapter.itemCount, equalTo(profiles.size))
        }
    }
}