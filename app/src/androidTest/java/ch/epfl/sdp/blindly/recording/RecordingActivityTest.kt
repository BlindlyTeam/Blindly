package ch.epfl.sdp.blindly.recording

import android.graphics.Color
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.matchers.EspressoTestMatchers.Companion.withDrawable
import ch.epfl.sdp.blindly.profile.ProfileFinished
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val AUDIO_FILE_ONE = "Audio file 1"
private const val MAXIMUM_AUDIO_DURATION = 90000L
private const val FIVE_SECONDS = 5000L

@RunWith(AndroidJUnit4::class)
class RecordingActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(RecordingActivity::class.java)

    @Test
    fun recordNameIsCorrectlyDisplayed() {
        createRecord(200L)
        val recordName = onView(withId(R.id.recordName))
        recordName.check(matches(withText(AUDIO_FILE_ONE)))
    }

    @Test
    fun recordingActivityFiresProfileFinished() {
        createRecord(200L)
        onView(withId(R.id.nameDurationLayout))
                .perform(click())

        Intents.init()
        onView(withId(R.id.selectButton))
                .perform(click())
        Intents.intended(IntentMatchers.hasComponent(ProfileFinished::class.java.name))
        Intents.release()
    }

    @Test
    fun playPauseButtonChangesBackgroundWhenClickedTwice() {
        createRecord(500L)
        onView(withId(R.id.nameDurationLayout))
                .perform(click())
        onView(withId(R.id.playPauseButton))
                .perform(click())
        onView(withId(R.id.playPauseButton))
                .perform(click())
        onView(withId(R.id.playPauseButton))
                .check(
                        matches(
                                withDrawable(android.R.drawable.ic_media_play)
                        )
                )
    }

    @Test
    fun playPauseButtonChangesBackgroundWhenPlayIsFinished() {
        createRecord(500L)
        onView(withId(R.id.nameDurationLayout))
                .perform(click())
        onView(withId(R.id.playPauseButton))
                .perform(click())
        Thread.sleep(2000L)
        onView(withId(R.id.playPauseButton))
                .check(
                        matches(
                                withDrawable(android.R.drawable.ic_media_play)
                        )
                )
    }

    @Test
    fun startRecordingCollapsesRecords() {
        createRecord(200L)
        onView(withId(R.id.nameDurationLayout))
                .perform(click())
        onView(withId(R.id.recordingButton))
                .perform(click())
        onView(withId(R.id.audioPlayLayout))
                .check(
                        matches(
                                not(isDisplayed())
                        )
                )
    }

    @Test
    fun maximumDurationStopsRecording() {
        val recordButton = onView(withId(R.id.recordingButton))
        recordButton.perform(click())
        Thread.sleep(MAXIMUM_AUDIO_DURATION + 500L)
        onView(withId(R.id.nameDurationLayout))
                .check(
                        matches(
                                isDisplayed()
                        )
                )
    }

    @Test
    fun remainingTimerIsRedWhen10SecondsRemain() {
        val recordButton = onView(withId(R.id.recordingButton))
        recordButton.perform(click())
        Thread.sleep(MAXIMUM_AUDIO_DURATION - FIVE_SECONDS)
        recordButton.perform(click())
        onView(withId(R.id.remainingTimer))
                .check(
                        matches(
                                (hasTextColor(Color.RED))
                        )
                )
    }

    private fun createRecord(duration: Long) {
        val recordButton = onView(withId(R.id.recordingButton))
        recordButton.perform(click())
        Thread.sleep(duration)
        recordButton.perform(click())
    }
}