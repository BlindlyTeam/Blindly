package ch.epfl.sdp.blindly.recording

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.RecyclerViewChildActions.Companion.actionOnChild
import ch.epfl.sdp.blindly.RecyclerViewChildActions.Companion.childOfViewAtPositionWithMatcher
import ch.epfl.sdp.blindly.matchers.EspressoTestMatchers.Companion.withDrawable
import ch.epfl.sdp.blindly.profile_setup.ProfileFinished
import ch.epfl.sdp.blindly.recording.RecordingActivity.Companion.AUDIO_DURATION_KEY
import junit.framework.Assert.fail
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.reflect.Method


private const val AUDIO_FILE_ONE = "Audio file 1"
private const val TEST_MAXIMUM_AUDIO_DURATION = 13000
private const val FIVE_SECONDS = 5000L
private const val TWO_SECONDS = 2000L


@RunWith(AndroidJUnit4::class)
class RecordingActivityTest {
    private val initBundle = Bundle()
    val intent = Intent(ApplicationProvider.getApplicationContext(), RecordingActivity::class.java)
        .putExtra(AUDIO_DURATION_KEY, TEST_MAXIMUM_AUDIO_DURATION)

    @get:Rule
    val activityRule = ActivityScenarioRule<RecordingActivity>(intent)

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
        Thread.sleep(TEST_MAXIMUM_AUDIO_DURATION + 500L)
        onView(withId(R.id.nameDurationLayout))
            .check(
                matches(
                    isDisplayed()
                )
            )
    }

    @Test
    fun remainingTimerIsRedWhen10SecondsRemain() {
        createRecord(TEST_MAXIMUM_AUDIO_DURATION - FIVE_SECONDS)
        onView(withId(R.id.remainingRecordTimer))
        onView(withId(R.id.remainingRecordTimer))
            .check(
                matches(
                    (hasTextColor(R.color.bright_red))
                )
            )
    }

    @Test
    fun startRecordingCollapsesAllRecordings() {
        createRecord(TWO_SECONDS)
        createRecord(TWO_SECONDS)
        //Click on the second recording to expand it
        onView(withId(R.id.recordingList)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                actionOnChild(
                    click(),
                    R.id.nameDurationLayout
                )
            )
        )
        //Start recording
        onView(withId(R.id.recordingButton)).perform(click())
        //Check if both recordings are collapsed
        onView(withId(R.id.recordingList)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.audioPlayLayout, 0, not(isDisplayed())
                )
            )
        )
        onView(withId(R.id.recordingList)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.audioPlayLayout, 1, not(isDisplayed())
                )
            )
        )

    }

    @Test
    fun clickOnARecordingCollapsesAllOthers() {
        createRecord(TWO_SECONDS)
        createRecord(TWO_SECONDS)
        //Click on the first recording to expand it
        onView(withId(R.id.recordingList)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                actionOnChild(
                    click(),
                    R.id.nameDurationLayout
                )
            )
        )
        //Click on the second recording to expand it
        onView(withId(R.id.recordingList)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                actionOnChild(
                    click(),
                    R.id.nameDurationLayout
                )
            )
        )
        //Check if first recording is collapsed
        onView(withId(R.id.recordingList)).check(
            matches(
                childOfViewAtPositionWithMatcher(
                    R.id.audioPlayLayout, 0, not(isDisplayed())
                )
            )
        )
    }

    @Test
    fun timerChangesWhenUserClicksOnBar() {
        createRecord(TWO_SECONDS)
        //Click on the first recording to expand it
        onView(withId(R.id.recordingList)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                actionOnChild(
                    click(),
                    R.id.nameDurationLayout
                )
            )
        )

        activityRule.scenario.onActivity { activity ->
            val playBar = activity.findViewById<SeekBar>(R.id.playBar)
            setSeekBarProgress(playBar, 50, true)
        }
    }

    private fun createRecord(duration: Long) {
        val recordButton = onView(withId(R.id.recordingButton))
        recordButton.perform(click())
        Thread.sleep(duration)
        recordButton.perform(click())
    }

    //Used to manually simulate a click from user and to set fromUser to 1
    private fun setSeekBarProgress(playBar: SeekBar, progress: Int, fromUser: Boolean) {
        var privateSetProgressMethod: Method? = null
        try {
            privateSetProgressMethod =
                ProgressBar::class.java.getDeclaredMethod(
                    "setProgressInternal",
                    Integer.TYPE,
                    java.lang.Boolean.TYPE,
                    java.lang.Boolean.TYPE
                )
            privateSetProgressMethod.isAccessible = true
            privateSetProgressMethod.invoke(playBar, progress, fromUser, false)
        } catch (e: ReflectiveOperationException) {
            e.printStackTrace()
            fail("Error while invoking private method.")
        }
    }

}