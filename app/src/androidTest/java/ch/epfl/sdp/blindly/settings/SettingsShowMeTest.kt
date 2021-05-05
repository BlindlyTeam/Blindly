package ch.epfl.sdp.blindly.settings

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import ch.epfl.sdp.blindly.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test

private const val TEST_SHOW_ME = "Women"

@HiltAndroidTest
class SettingsShowMeTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun showMeFromIntentIsDisplayedProperly() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), SettingsShowMe::class.java)
        intent.putExtra(EXTRA_SHOW_ME, TEST_SHOW_ME)

        ActivityScenario.launch<SettingsShowMe>(intent)
        onView(withId(R.id.checkmark_img_women))
            .check(matches(isDisplayed()))
        onView(withId(R.id.checkmark_img_men))
            .check(matches(not(isDisplayed())))
        onView(withId(R.id.checkmark_img_everyone))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun onButtonClickShowMeCheckChanges() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), SettingsShowMe::class.java)
        intent.putExtra(EXTRA_SHOW_ME, TEST_SHOW_ME)
        ActivityScenario.launch<SettingsShowMe>(intent)

        onView((withId(R.id.show_me_everyone_button)))
            .perform(click())
        onView(withId(R.id.checkmark_img_women))
            .check(matches(not(isDisplayed())))
        onView(withId(R.id.checkmark_img_men))
            .check(matches(not(isDisplayed())))
        onView(withId(R.id.checkmark_img_everyone))
            .check(matches(isDisplayed()))
    }
}