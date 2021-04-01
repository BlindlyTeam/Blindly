package ch.epfl.sdp.blindly.recording

import android.view.View
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
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
        val text = recordingText.toString()
        recordingText.check(matches(not(isDisplayed())))
        recordButton.perform(click())
        recordingText.check(matches(isDisplayed()))
        //recordingText.check(matches(withText("Recording&#8230;")))
        Thread.sleep(500)
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

    /*@Test
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
    }*/ //TODO playButton waits for the player to be done playing before clicking again

    @Test
    fun seekBarClickChangesTheProgress() {
        val recordButton = Espresso.onView(withId(R.id.recordingButton))
        val playButton = Espresso.onView(withId(R.id.playingButton))
        val seekBar = Espresso.onView(withId(R.id.playBar))

        recordButton.perform(click())
        Thread.sleep(2000)
        recordButton.perform(click())

        playButton.perform(click())
        seekBar.perform(setProgress(1000))
    }

    // ViewAction to manually set the progress of the seekBar
    private fun setProgress(progress: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(SeekBar::class.java)
            }

            override fun getDescription(): String {
                return "Set a progress on a SeekBar"
            }

            override fun perform(uiController: UiController?, view: View?) {
                val seekBar = view as SeekBar
                seekBar.progress = progress
            }
        }
    }
}

