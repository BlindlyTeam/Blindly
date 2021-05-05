package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.os.Bundle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.BundleMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.user.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

const val TEST_BIRTHDAY = "18.03.2003"
private const val ERROR_AGE = "The minimum age requirement is 18 years old."

@RunWith(AndroidJUnit4::class)
class TestProfileBirthday {

    private val TEST_USER = User.Builder().setUsername(CORRECT_NAME)

    @Before
    fun setup() {
        val bundle = Bundle()
        bundle.putSerializable(
            EXTRA_USER,
            Json.encodeToString(TEST_USER)
        )

        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            ProfileBirthday::class.java
        ).apply {
            putExtras(bundle)
        }

        ActivityScenario.launch<ProfileBirthday>(intent)
        init()
    }

    @After
    fun afterEach() {
        release()
    }

    @Test
    fun minorAgeOutputsError() {
        onView(withId(R.id.datePicker)).perform(PickerActions.setDate(2005, 3, 20))
        val buttonContinue = onView(withId(R.id.button_p3))
        buttonContinue.perform(click())
        intended(hasComponent(ProfileGender::class.java.name), times(0))
        onView(withId(R.id.warning_p3)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(
                    Matchers.containsString(
                        ERROR_AGE
                    )
                )
            )
        )
    }

    @Test
    fun adultAgeFiresProfileGender() {
        onView(withId(R.id.datePicker)).perform(PickerActions.setDate(2003, 3, 18))
        onView(withId(R.id.button_p3)).perform(click())

        TEST_USER.setBirthday(TEST_BIRTHDAY)

        intended(
            Matchers.allOf(
                hasComponent(ProfileGender::class.java.name),
                IntentMatchers.hasExtras(
                    BundleMatchers.hasEntry(
                        EXTRA_USER,
                        Json.encodeToString(TEST_USER)
                    )
                )
            )
        )
    }

}