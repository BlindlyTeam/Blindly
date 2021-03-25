package ch.epfl.sdp.blindly.settings

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.FakeUserHelperModule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.profile.Profile3
import ch.epfl.sdp.blindly.utils.UserHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class SettingsUpdateEmailTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(SettingsUpdateEmail::class.java)
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var user: UserHelper

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun emailIsCorrect() {
        init()
        onView(ViewMatchers.withId(R.id.update_email_field)).check(
                ViewAssertions.matches(
                        ViewMatchers.withHint(
                                user.getEmail()
                        )
                )
        )
        release()
    }

    @Test
    fun emailUpdateWorks() {
        init()
        onView(ViewMatchers.withId(R.id.update_email_field)).check(
                ViewAssertions.matches(
                        ViewMatchers.withHint(
                                user.getEmail()
                        )
                )
        )
        onView(ViewMatchers.withId(R.id.update_email_field))
                .perform(ViewActions.clearText(), ViewActions.typeText(FakeUserHelperModule.SECOND_EMAIL));

        closeSoftKeyboard();
        val buttonUpdate = Espresso.onView(ViewMatchers.withId(R.id.update_email_button))
        buttonUpdate.perform(ViewActions.click())
        Thread.sleep(2000)
        onView(ViewMatchers.withId(R.id.update_email_success_notice)).check(
                ViewAssertions.matches(
                        Matchers.allOf(
                        ViewMatchers.withText(R.string.email_update_success),
                        ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
                ))
        );
        release()
    }
}