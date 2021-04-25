package ch.epfl.sdp.blindly.profile_setup

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.recording.RecordingActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestProfileAudioRecording {
    @get:Rule
    val activityRule = ActivityScenarioRule(ProfileAudioRecording::class.java)

    @Before
    fun setup() {
        init()
    }

    @After
    fun afterEach() {
        release()
    }

    @Test
    fun testProfileAudioRecordingFiresRecordingActivity() {
        val buttonContinue = Espresso.onView(ViewMatchers.withId(R.id.button_p8))
        buttonContinue.perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(RecordingActivity::class.java.name))
    }
}