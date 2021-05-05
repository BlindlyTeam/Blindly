package ch.epfl.sdp.blindly.settings

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import ch.epfl.sdp.blindly.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test

private const val TEST_LOCATION = "Lausanne, Switzerland"

@HiltAndroidTest
class SettingsLocationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun showMeFromIntentIsDisplayedProperly() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), SettingsLocation::class.java)
        intent.putExtra(EXTRA_LOCATION, TEST_LOCATION)

        ActivityScenario.launch<SettingsLocation>(intent)
        onView(withId(R.id.my_current)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(
                    Matchers.containsString(
                        TEST_LOCATION
                    )
                )
            )
        )
    }

    @Test
    fun mapZoomsInWhenDoubleTapping() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), SettingsLocation::class.java)
        intent.putExtra(EXTRA_LOCATION, TEST_LOCATION)

        ActivityScenario.launch<SettingsLocation>(intent)
        Thread.sleep(1000)
        onView(withId(R.id.map)).perform(click(), click())
        Thread.sleep(1000)
    }
}