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

private const val ERROR_MESSAGE_1 = "Please select at least one!"
private const val ERROR_MESSAGE_2 = "You can not select more than 5!"
@HiltAndroidTest
class TestProfilePassions {

    private val TEST_USER = User.Builder()
            .setUsername(CORRECT_NAME)
            .setBirthday(TEST_BIRTHDAY)
            .setGender(TEST_GENDER_WOMEN)
            .setSexualOrientations(TEST_SEXUAL_ORIENTATIONS)
    private val SERIALIZED = Json.encodeToString(TEST_USER)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }


    @Before
    fun init() {
        val bundle = Bundle()
        bundle.putSerializable(EXTRA_USER, SERIALIZED)

        val intent = Intent(ApplicationProvider.getApplicationContext(),
                ProfilePassions::class.java).apply {
            putExtras(bundle)
        }

        ActivityScenario.launch<ProfilePassions>(intent)
    }


    @Test
    fun noInputShowsError() {
        Intents.init()
        val buttonContinue = onView(withId(R.id.button_p7))
        buttonContinue.perform(click())

        onView(withId(R.id.warning_p7_1))
                .check(
                        ViewAssertions.matches(
                                ViewMatchers.withText(
                                        Matchers.containsString(
                                                ERROR_MESSAGE_1
                                        )
                                )
                        )
                )
        intended(hasComponent(ProfileAudioRecording::class.java.name), Intents.times(0))
        Intents.release()
    }

    @Test
    fun moreThanAllowedInputShowsError() {
        Intents.init()

        onView(withId(R.id.chip10)).perform(click())
        onView(withId(R.id.chip11)).perform(click())
        onView(withId(R.id.chip12)).perform(click())
        onView(withId(R.id.chip13)).perform(click())
        onView(withId(R.id.chip14)).perform(click())
        onView(withId(R.id.chip15)).perform(click())

        val buttonContinue = onView(withId(R.id.button_p7))
        buttonContinue.perform(click())

        onView(withId(R.id.warning_p7_2))
                .check(
                        ViewAssertions.matches(
                                ViewMatchers.withText(
                                        Matchers.containsString(
                                                ERROR_MESSAGE_2
                                        )
                                )
                        )
                )
        intended(hasComponent(ProfileAudioRecording::class.java.name), Intents.times(0))
        Intents.release()

    }

    @Test
    fun correctInputsFiresProfileAudioRecording() {
        Intents.init()

        onView(withId(R.id.chip10)).perform(click())
        onView(withId(R.id.chip11)).perform(click())
        onView(withId(R.id.chip12)).perform(click())

        val buttonContinue = onView(withId(R.id.button_p7))
        buttonContinue.perform(click())

        intended(hasComponent(ProfileAudioRecording::class.java.name))
        Intents.release()
    }

}