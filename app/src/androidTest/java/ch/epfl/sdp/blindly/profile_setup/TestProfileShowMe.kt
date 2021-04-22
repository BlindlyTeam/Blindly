package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.os.Bundle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
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
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.user.User
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val TEST_SHOW_ME = "Women"
private const val NO_INPUT_ERROR = "Please select one!"

@HiltAndroidTest
class TestProfileShowMe {

    private val TEST_USER = User.Builder()
            .setUsername(CORRECT_NAME)
            .setBirthday(TEST_BIRTHDAY)
    private val SERIALIZED = Json.encodeToString(TEST_USER)

    @Before
    fun init() {
        val bundle = Bundle()
        bundle.putSerializable(EXTRA_USER, SERIALIZED)

        val intent = Intent(ApplicationProvider.getApplicationContext(),
                ProfileShowMe::class.java).apply {
            putExtras(bundle)
        }

        ActivityScenario.launch<ProfileShowMe>(intent)
    }


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

        TEST_USER.setShowMe(TEST_SHOW_ME)

        intended(Matchers.allOf(
            hasComponent(ProfilePassions::class.java.name),
                IntentMatchers.hasExtras(
                    BundleMatchers.hasEntry(EXTRA_USER, Json.encodeToString(TEST_USER)))))
        Intents.release()
    }
}