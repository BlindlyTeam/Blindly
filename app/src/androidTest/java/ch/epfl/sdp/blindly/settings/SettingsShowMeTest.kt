package ch.epfl.sdp.blindly.settings

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.profile.EXTRA_SHOW_ME
import ch.epfl.sdp.blindly.profile_preferences.TEST_SHOW_ME
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsShowMeTest {

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