package ch.epfl.sdp.blindly.recording

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.profile.ProfileFinished
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val FIVE_SECONDS_TEXT = "00:05"
private const val AUDIO_FILE_ONE = "Audio file 1"

@RunWith(AndroidJUnit4::class)
class RecordingActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(RecordingActivity::class.java)

    @Test
    fun recordDurationTextIsCorrectlyDisplayed() {
        val recordButton = Espresso.onView(withId(R.id.recordingButton))
        val recordTimer = Espresso.onView(withId(R.id.recordTimer))
        recordButton.perform(click())
        Thread.sleep(5200)
        recordButton.perform(click())
        recordTimer.check(matches(withText(FIVE_SECONDS_TEXT)))
    }

    @Test
    fun recordNameIsCorrectlyDisplayed() {
        val recordButton = Espresso.onView(withId(R.id.recordingButton))
        val recordName = Espresso.onView(withId(R.id.recordName))
        recordButton.perform(click())
        Thread.sleep(1200)
        recordButton.perform(click())
        recordName.check(matches(withText(AUDIO_FILE_ONE)))
    }

    @Test
    fun audioDurationTextIsCorrectlyDisplayed() {
        val recordButton = Espresso.onView(withId(R.id.recordingButton))
        val recordDuration = Espresso.onView(withId(R.id.recordDuration))
        recordButton.perform(click())
        Thread.sleep(5200)
        recordButton.perform(click())
        recordDuration.check(matches(withText(FIVE_SECONDS_TEXT)))
    }

    @Test
    fun recordingActivityFiresProfileFinished() {
        val recordButton = Espresso.onView(withId(R.id.recordingButton))
        recordButton.perform(click())
        Thread.sleep(1000)
        recordButton.perform(click())
        Espresso.onView(withId(R.id.nameDurationLayout))
            .perform(click())

        Intents.init()
        Espresso.onView(withId(R.id.selectButton))
            .perform(click())
        Intents.intended(IntentMatchers.hasComponent(ProfileFinished::class.java.name))
        Intents.release()
    }
}