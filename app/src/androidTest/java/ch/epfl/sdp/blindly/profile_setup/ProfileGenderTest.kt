package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.os.Bundle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
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

private const val NO_INPUT_ERROR = "Please select one!"
private const val TEST_GENDER_MORE = "More"
private const val TEST_GENDER_MEN = "Man"
const val TEST_GENDER_WOMEN = "Woman"

@RunWith(AndroidJUnit4::class)
class TestProfileGender {
    private val TEST_USER = User.Builder()
        .setUsername(CORRECT_NAME)
        .setBirthday(TEST_BIRTHDAY)

    @Before
    fun setup() {
        val bundle = Bundle()
        bundle.putSerializable(EXTRA_USER, Json.encodeToString(TEST_USER))

        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            ProfileGender::class.java
        ).apply {
            putExtras(bundle)
        }

        ActivityScenario.launch<ProfileGender>(intent)
        init()
    }

    @After
    fun afterEach() {
        release()
    }

    @Test
    fun noInputOutputsError() {
        val buttonContinue = Espresso.onView(withId(R.id.button_p4))
        buttonContinue.perform(click())

        intended(hasComponent(ProfileOrientation::class.java.name), times(0))
        intended(hasComponent(ProfileGenderMore::class.java.name), times(0))

        Espresso.onView(withId(R.id.warning_p4))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.withText(
                        Matchers.containsString(
                            NO_INPUT_ERROR
                        )
                    )
                )
            )
    }

    @Test
    fun womanFiresProfileOrientation() {
        val buttonWoman = Espresso.onView(withId(R.id.sex1_user))
        buttonWoman.perform(click())
        val buttonContinue = Espresso.onView(withId(R.id.button_p4))
        buttonContinue.perform(click())

        TEST_USER.setGender(TEST_GENDER_WOMEN)

        intended(
            Matchers.allOf(
                hasComponent(ProfileOrientation::class.java.name),
                IntentMatchers.hasExtras(
                    BundleMatchers.hasEntry(EXTRA_USER, Json.encodeToString(TEST_USER))
                )
            )
        )
    }

    @Test
    fun manFiresProfileOrientation() {
        val buttonWoman = Espresso.onView(withId(R.id.sex2_user))
        buttonWoman.perform(click())
        val buttonContinue = Espresso.onView(withId(R.id.button_p4))
        buttonContinue.perform(click())

        TEST_USER.setGender(TEST_GENDER_MEN)

        intended(
            Matchers.allOf(
                hasComponent(ProfileOrientation::class.java.name),
                IntentMatchers.hasExtras(
                    BundleMatchers.hasEntry(EXTRA_USER, Json.encodeToString(TEST_USER))
                )
            )
        )
    }

    @Test
    fun moreFiresProfileGenderMore() {
        val buttonMore = Espresso.onView(withId(R.id.sex3_more))
        buttonMore.perform(click())
        val buttonContinue = Espresso.onView(withId(R.id.button_p4))
        buttonContinue.perform(click())

        TEST_USER.setGender(TEST_GENDER_MORE)

        intended(
            Matchers.allOf(
                hasComponent(ProfileGenderMore::class.java.name),
                IntentMatchers.hasExtras(
                    BundleMatchers.hasEntry(EXTRA_USER, Json.encodeToString(TEST_USER))
                )
            )
        )
    }
}