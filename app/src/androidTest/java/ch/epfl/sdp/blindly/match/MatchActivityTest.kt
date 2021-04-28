package ch.epfl.sdp.blindly.match

import android.content.Intent
import android.os.Parcelable
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import ch.epfl.sdp.blindly.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.ArrayList

const val SLEEP_TIME = 250L

@HiltAndroidTest
class MatchActivityTest {

    private val profiles = createProfiles()


    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
        launchScenario()
    }

    @Test
    fun firstCardIsWellDisplayed() {
        onView(withId(R.id.card_stack_view)).check(
            matches(
                hasDescendant(
                    withText(profiles[0].name)
                )
            )
        )
        onView(withId(R.id.card_stack_view)).check(
            matches(
                hasDescendant(
                    withText(profiles[0].age.toString())
                )
            )
        )
    }

    @Test
    fun skipButtonDisplaysSecondCard() {
        onView(withId(R.id.skip_button)).perform(click())
        Thread.sleep(SLEEP_TIME)
        onView(withId(R.id.card_stack_view)).check(
            matches(
                hasDescendant(
                    withText(profiles[1].name)
                )
            )
        )
        onView(withId(R.id.card_stack_view)).check(
            matches(
                hasDescendant(
                    withText(profiles[1].age.toString())
                )
            )
        )
    }

    @Test
    fun likeButtonDisplaysSecondCard() {
        onView(withId(R.id.like_button)).perform(click())
        Thread.sleep(SLEEP_TIME)
        onView(withId(R.id.card_stack_view)).check(
            matches(
                hasDescendant(
                    withText(profiles[1].name)
                )
            )
        )
        onView(withId(R.id.card_stack_view)).check(
            matches(
                hasDescendant(
                    withText(profiles[1].age.toString())
                )
            )
        )
    }

    @Test
    fun rewindButtonHasNoEffectOnFirstCard() {
        onView(withId(R.id.rewind_button)).perform(click())
        Thread.sleep(SLEEP_TIME)
        onView(withId(R.id.card_stack_view)).check(
            matches(
                hasDescendant(
                    withText(profiles[0].name)
                )
            )
        )
        onView(withId(R.id.card_stack_view)).check(
            matches(
                hasDescendant(
                    withText(profiles[0].age.toString())
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
            matches(
                hasDescendant(
                    withText(profiles[0].name)
                )
            )
        )
        onView(withId(R.id.card_stack_view)).check(
            matches(
                hasDescendant(
                    withText(profiles[0].age.toString())
                )
            )
        )
    }

    @Test
    fun swipeWorksAsButtons() {
        onView(withId(R.id.card_stack_view)).perform(swipeLeft())
        Thread.sleep(SLEEP_TIME)
        onView(withId(R.id.card_stack_view)).check(
            matches(
                hasDescendant(
                    withText(profiles[1].name)
                )
            )
        )
        onView(withId(R.id.card_stack_view)).check(
            matches(
                hasDescendant(
                    withText(profiles[1].age.toString())
                )
            )
        )
    }

    private fun launchScenario() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MatchActivity::class.java)
        intent.putParcelableArrayListExtra(
            "EXTRA_MATCH_PROFILES",
            profiles as ArrayList<out Parcelable>
        )
        ActivityScenario.launch<MatchActivity>(intent)
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
