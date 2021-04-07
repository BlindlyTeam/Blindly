package ch.epfl.sdp.blindly.profile_setup

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.BundleMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.utils.UserHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

const val TEST_SHOW_ME = "Women"
private const val NO_INPUT_ERROR = "Please select one!"

@HiltAndroidTest
class TestProfileShowMe {

    @get:Rule
    val activityRule = ActivityScenarioRule(ProfileShowMe::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun noInputOutputsError() {
        Intents.init()
        val buttonContinue = onView(withId(R.id.button_p6))
        buttonContinue.perform(click())
        intended(hasComponent(ProfilePassions::class.java.name), Intents.times(0))
        onView(withId(R.id.warning_p6))
                .check(
                        ViewAssertions.matches(
                                ViewMatchers.withText(
                                        Matchers.containsString(
                                                NO_INPUT_ERROR
                                        )
                                )
                        )
                )
        Intents.release()
    }

    @Test
    fun anyChoiceFiresProfilePassions() {
        Intents.init()
        val buttonPref = onView(withId(R.id.sex1_pref))
        buttonPref.perform(click())
        val buttonContinue = onView(withId(R.id.button_p6))
        buttonContinue.perform(click())
        intended(Matchers.allOf(hasComponent(ProfilePassions::class.java.name),
                IntentMatchers.hasExtras(BundleMatchers.hasEntry(EXTRA_SHOW_ME, TEST_SHOW_ME))))
        Intents.release()
    }
}