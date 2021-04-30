package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.os.Bundle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.main_screen.MainScreen
import ch.epfl.sdp.blindly.user.User
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class TestProfileFinished {
    private val TEST_USER = User.Builder()
        .setUsername(CORRECT_NAME)
        .setBirthday(TEST_BIRTHDAY)
        .setGender(TEST_GENDER_WOMEN)
        .setSexualOrientations(TEST_SEXUAL_ORIENTATIONS)
        .setPassions(TEST_PASSIONS)
    private val SERIALIZED = Json.encodeToString(TEST_USER)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
        val bundle = Bundle()
        bundle.putSerializable(EXTRA_USER, SERIALIZED)

        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            ProfileFinished::class.java
        ).apply {
            putExtras(bundle)
        }

        ActivityScenario.launch<ProfileFinished>(intent)
        init()
    }

    @After
    fun afterEach() {
        release()
    }

    @Test
    fun profileFinishedFiresMainScreen() {
        val buttonMainScreen = Espresso.onView(ViewMatchers.withId(R.id.buttonMainScreen))
        buttonMainScreen.perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(MainScreen::class.java.name))
    }
}