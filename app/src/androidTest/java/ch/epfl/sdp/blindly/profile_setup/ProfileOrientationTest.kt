package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.os.Bundle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
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

private const val ERROR_MESSAGE_1 = "Please select at least one!"
private const val ERROR_MESSAGE_2 = "You can not select more than 3!"
val TEST_SEXUAL_ORIENTATIONS = arrayListOf("Straight", "Lesbian", "Gay")

@RunWith(AndroidJUnit4::class)
class TestProfileOrientation {
    private val TEST_USER = User.Builder()
        .setUsername(CORRECT_NAME)
        .setBirthday(TEST_BIRTHDAY)
        .setGender(TEST_GENDER_WOMEN)

    @Before
    fun setup() {
        val bundle = Bundle()
        bundle.putSerializable(EXTRA_USER, Json.encodeToString(TEST_USER))

        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            ProfileOrientation::class.java
        ).apply {
            putExtras(bundle)
        }

        ActivityScenario.launch<ProfileOrientation>(intent)
        init()
    }

    @After
    fun afterEach() {
        release()
    }

    @Test
    fun noInputOutputsError() {
        val buttonContinue = onView(withId(R.id.button_p5))
        buttonContinue.perform(click())

        onView(withId(R.id.at_least_1_warning))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.withText(
                        Matchers.containsString(
                            ERROR_MESSAGE_1
                        )
                    )
                )
            )
        intended(hasComponent(ProfileShowMe::class.java.name), Intents.times(0))
    }

    @Test
    fun moreThanAllowedInputOutputsError() {
        onView(withId(R.id.straight_chip)).perform(click())
        onView(withId(R.id.lesbian_chip)).perform(click())
        onView(withId(R.id.gay_chip)).perform(click())
        onView(withId(R.id.bisexual_chip)).perform(click())

        val buttonContinue = onView(withId(R.id.button_p5))
        buttonContinue.perform(click())

        onView(withId(R.id.no_more_than_3_warning))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.withText(
                        Matchers.containsString(
                            ERROR_MESSAGE_2
                        )
                    )
                )
            )
        intended(hasComponent(ProfileShowMe::class.java.name), Intents.times(0))
    }

    @Test
    fun correctInputFiresProfileShowMe() {
        onView(withId(R.id.straight_chip)).perform(click())
        onView(withId(R.id.lesbian_chip)).perform(click())
        onView(withId(R.id.gay_chip)).perform(click())

        val buttonContinue = onView(withId(R.id.button_p5))
        buttonContinue.perform(click())

        TEST_USER.setSexualOrientations(TEST_SEXUAL_ORIENTATIONS)

        intended(
            Matchers.allOf(
                hasComponent(ProfileShowMe::class.java.name),
                IntentMatchers.hasExtras(
                    BundleMatchers.hasEntry(EXTRA_USER, Json.encodeToString(TEST_USER))
                )
            )
        )
    }
}