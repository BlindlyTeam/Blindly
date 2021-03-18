package ch.epfl.sdp.blindly.recording

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.R
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecordingActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(RecordingActivity::class.java)

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
        recordButton.perform(click())
        Thread.sleep(2000)
        recordButton.perform(click())
        playButton.check(matches(withText("Play")))
        playButton.perform(click())
        playButton.check(matches(withText("Pause")))
        playButton.perform(click())
        playButton.check(matches(withText("Play")))
    }
}