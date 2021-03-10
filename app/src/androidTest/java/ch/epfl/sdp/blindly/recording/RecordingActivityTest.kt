package ch.epfl.sdp.blindly.recording

import android.content.Intent
import android.content.pm.PackageManager
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.R
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

@RunWith(AndroidJUnit4::class)
class RecordingActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(RecordingActivity::class.java)

    @Test
    fun onCreateWorks() {
        // TODO
    }

    // Permissions test seem to not pass I don't know why
    @Test
    fun onRequestPermissionsResultGrantsPermissionCorrectly() {
        val recordingActivity = RecordingActivity()
        val permissions = arrayOf<String>()
        val grantResults = IntArray(2)
        recordingActivity.onRequestPermissionsResult(REQUEST_RECORD_AUDIO_PERMISSION, permissions,
                grantResults)
        assertThat(recordingActivity.permissionToRecordAccepted,
                equalTo(grantResults[0] == PackageManager.PERMISSION_GRANTED))
    }

    @Test
    fun onRequestPermissionResultDeniesPermissionCorrectly() {
        val recordingActivity = RecordingActivity()
        val permissions = arrayOf<String>()
        val grantResults = IntArray(2)
        recordingActivity.onRequestPermissionsResult(0, permissions,
                grantResults)
        assertThat(recordingActivity.permissionToRecordAccepted, equalTo(false))
    }

    @Test
    fun onStopWorks() {
        // TODO
    }

    @Test
    fun recordingTextIsCorrectlyDisplayed() {
        val recordButton = Espresso.onView(withId(R.id.recordingButton))
        val recordingText = Espresso.onView(withId(R.id.recordingText))
        recordingText.check(matches(not(isDisplayed())))
        recordButton.perform(click())
        recordingText.check(matches(isDisplayed()))
        recordingText.check(matches(withText("Recording...")))
        recordButton.perform(click())
        recordingText.check(matches(withText("Done !")))
    }

    @Test
    fun recordingButtonTextIsCorrectlyDisplayed() {
        val recordButton = Espresso.onView(withId(R.id.recordingButton))
        recordButton.check(matches(withText("Start recording")))
        recordButton.perform(click())
        recordButton.check(matches(withText("Stop recording")))
        recordButton.perform(click())
        recordButton.check(matches(withText("Start recording")))
    }

    @Test
    fun playingButtonIsCorrectlyDisabled() {
        val playButton = Espresso.onView(withId(R.id.playingButton))
        playButton.check(matches(not(isEnabled())))
    }

    @Test
    fun playingButtonIsCorrectlyEnabled() {
        val recordButton = Espresso.onView(withId(R.id.recordingButton))
        val playButton = Espresso.onView(withId(R.id.playingButton))
        recordButton.perform(click(), click())
        playButton.check(matches(isEnabled()))
    }

    @Test
    fun playingButtonTextIsCorrectlyDisplayed() {
        val recordButton = Espresso.onView(withId(R.id.recordingButton))
        val playButton = Espresso.onView(withId(R.id.playingButton))
        recordButton.perform(click(), click())
        playButton.check(matches(withText("Start playing")))
        playButton.perform(click())
        playButton.check(matches(withText("Stop playing")))
        playButton.perform(click())
        playButton.check(matches(withText("Start playing")))
    }
}