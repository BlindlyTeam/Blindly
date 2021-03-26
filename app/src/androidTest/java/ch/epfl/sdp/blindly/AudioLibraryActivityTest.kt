package ch.epfl.sdp.blindly

import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindly.R
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class AudioLibraryActivityTest {
    @get : Rule
    val activityRule = ActivityScenarioRule(AudioLibrary::class.java)

    private val PATH = "${getApplicationContext<Context>().filesDir.absolutePath}"

    @Before
    fun dummyRecordings(){
        // Create 2 dummy file to show in the RecyclerView
        File(PATH, "DUMMY_0.txt").printWriter().use {
            out -> out.println("Dummy")
        }
        File(PATH, "DUMMY_1.txt").printWriter().use {
            out -> out.println("Dummy")
        }
    }

    @After
    fun deleteDummyRecordings(){
        // Delete the 2 dummy files when done with testing
        File(PATH, "DUMMY_0.txt").delete()
        File(PATH, "DUMMY_1.txt").delete()
    }

     // RecyclerView comes into view
    @Test
    fun isListVisibleOnLauch(){
        onView(withId(R.id.recordingList)).check(matches(isDisplayed()))
    }

    /**
     * Select list item makes the check visible
     */
    @Test
    fun clickOnItemSelectsIt(){
        onView(withId(R.id.recordingList))
                .perform(actionOnItemAtPosition<RecordingAdapter.ViewHolder>(0, click()))
        //onView(withId(R.id.recording))
    }

    /**
     * Select list twice plays and pause
     */

    /**
     * Select list item, then another one plays the second one
     */

    /**
     * Click select when no selection creates a toast
     */

    /**
     * Click the selection saves the current selection
     */
}