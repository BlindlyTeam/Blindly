package ch.epfl.sdp.blindly

import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Rule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.hamcrest.core.StringContains
import org.junit.Test

class UserHelperTest {
    @get:Rule
    var activityScenarioRule = ActivityScenarioRule<Activity>(ch.epfl.sdp.blindly.utils.UserHelper.Companion.getSignInIntent());

    @Test
    fun testGoogleAcountButtonPresent() {
        onView(withText(StringContains("Google")))
            .check(matches(isDisplayed()))
            .check(matches(isClickable()))
    }
    @Test
    fun testPhoneAcountButtonPresent() {
        onView(withText(StringContains("Phone")))
            .check(matches(isDisplayed()))
            .check(matches(isClickable()))
    }

}