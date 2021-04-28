package ch.epfl.sdp.blindly.chat

import android.content.Intent
import android.os.Bundle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
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
class ChatActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(ChatActivity::class.java)

    @Before
    fun setup() {
        val bundle = Bundle()
        bundle.putString("matchId", "kh5EpYDCqXNtKWKTUYA02Kp65NB3")

        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            ChatActivity::class.java
        ).apply {
            putExtras(bundle)
        }

        ActivityScenario.launch<ChatActivity>(intent)
        Intents.init()
    }

    @After
    fun afterEach() {
        Intents.release()
    }

    @Test
    fun trivialTest(){
    }

}