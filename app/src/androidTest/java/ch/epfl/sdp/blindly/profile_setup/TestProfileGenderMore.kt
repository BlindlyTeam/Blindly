package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.os.Bundle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.times
import androidx.test.espresso.intent.matcher.BundleMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.user.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val CORRECT_SPECIFICATION = "Abcde"
private const val BLANK_SPECIFICATION = "   "
private const val INCORRECT_CHARS_SPECIFICATION = "Abc;;de"
private const val NO_INPUT = ""
private const val ERROR_MESSAGE = "Please specify!"
private const val ERROR_CHARACTERS = "Please use only letters."

@RunWith(AndroidJUnit4::class)
class TestProfileGenderMore {

    private val TEST_USER = User.Builder()
            .setUsername(CORRECT_NAME)
            .setBirthday(TEST_BIRTHDAY)

    @Before
    fun init() {
        val bundle = Bundle()
        bundle.putSerializable(EXTRA_USER, Json.encodeToString(TEST_USER))

        val intent = Intent(ApplicationProvider.getApplicationContext(),
                ProfileGenderMore::class.java).apply {
            putExtras(bundle)
        }

        ActivityScenario.launch<ProfileGenderMore>(intent)
    }


    @Test
    fun incorrectCharOutputsError() {
        Intents.init()
        onView(withId(R.id.text_p4_2)).perform(clearText(), typeText(INCORRECT_CHARS_SPECIFICATION))
        closeSoftKeyboard()
        val buttonContinue = onView(withId(R.id.button_p4_2))
        buttonContinue.perform(click())
        onView(withId(R.id.warning2_p4_2))
                .check(
                        ViewAssertions.matches(
                                ViewMatchers.withText(
                                        Matchers.containsString(
                                                ERROR_CHARACTERS
                                        )
                                )
                        )
                )

        intended(hasComponent(ProfileOrientation::class.java.name), times(0))
        Intents.release()
    }

    @Test
    fun noInputOutputsError() {
        Intents.init()
        onView(withId(R.id.text_p4_2)).perform(clearText(), typeText(NO_INPUT))
        closeSoftKeyboard()
        val buttonContinue = onView(withId(R.id.button_p4_2))
        buttonContinue.perform(click())
        onView(withId(R.id.warning1_p4_2))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.withText(
                        Matchers.containsString(ERROR_MESSAGE))))

        intended(hasComponent(ProfileOrientation::class.java.name), times(0))
        Intents.release()
    }


    @Test
    fun blankInputOutputsError() {
        Intents.init()
        onView(withId(R.id.text_p4_2)).perform(clearText(), typeText(BLANK_SPECIFICATION))
        closeSoftKeyboard()
        val buttonContinue = onView(withId(R.id.button_p4_2))
        buttonContinue.perform(click())
        onView(withId(R.id.warning1_p4_2))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.withText(
                        Matchers.containsString(ERROR_MESSAGE))))

        intended(hasComponent(ProfileOrientation::class.java.name), times(0))
        Intents.release()
    }

    @Test
    fun correctInputFiresProfileOrientation() {
        Intents.init()
        TEST_USER.setGender(CORRECT_SPECIFICATION)

        onView(withId(R.id.text_p4_2)).perform(clearText(), typeText(CORRECT_SPECIFICATION))
        closeSoftKeyboard()
        val buttonContinue = onView(withId(R.id.button_p4_2))
        buttonContinue.perform(click())

        TEST_USER.setGender(CORRECT_SPECIFICATION)

        intended(Matchers.allOf(
            hasComponent(ProfileOrientation::class.java.name),
            IntentMatchers.hasExtras(
                BundleMatchers.hasEntry(EXTRA_USER, Json.encodeToString(TEST_USER)))))
        Intents.release()
    }

}