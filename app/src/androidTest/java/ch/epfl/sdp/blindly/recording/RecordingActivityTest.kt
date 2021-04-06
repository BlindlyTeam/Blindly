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

private const val TWO_SECONDS_TEXT = "00:02"
private const val AUDIO_FILE_ONE = "Audio file 1"

@RunWith(AndroidJUnit4::class)
class RecordingActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(RecordingActivity::class.java)

    @Test
    fun recordDurationTextIsCorrectlyDisplayed() {
        val recordTimer = Espresso.onView(withId(R.id.recordTimer))
        createRecord(2200L)
        recordTimer.check(matches(withText(TWO_SECONDS_TEXT)))
    }

    @Test
    fun recordNameIsCorrectlyDisplayed() {
        createRecord(200L)
        val recordName = Espresso.onView(withId(R.id.recordName))
        recordName.check(matches(withText(AUDIO_FILE_ONE)))
    }

    @Test
    fun audioDurationTextIsCorrectlyDisplayed() {
        createRecord(2200L)
        val recordDuration = Espresso.onView(withId(R.id.recordDuration))
        recordDuration.check(matches(withText(TWO_SECONDS_TEXT)))
    }

    @Test
    fun recordingActivityFiresProfileFinished() {
        createRecord(200L)
        Espresso.onView(withId(R.id.nameDurationLayout))
            .perform(click())

        Intents.init()
        Espresso.onView(withId(R.id.selectButton))
            .perform(click())
        Intents.intended(IntentMatchers.hasComponent(ProfileFinished::class.java.name))
        Intents.release()
    }

    @Test
    fun playPauseButtonChangesBackgroundWhenClicked() {
        createRecord(500L)
        Espresso.onView(withId(R.id.nameDurationLayout))
                .perform(click())
        Espresso.onView(withId(R.id.playPauseButton))
                .perform(click())
        Espresso.onView(withId(R.id.playPauseButton))
                .check(

                )
    }

    private fun createRecord(duration: Long) {
        val recordButton = Espresso.onView(withId(R.id.recordingButton))
        recordButton.perform(click())
        Thread.sleep(duration)
        recordButton.perform(click())
    }
}