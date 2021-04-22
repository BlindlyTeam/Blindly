package ch.epfl.sdp.blindly

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.profile_setup.ProfileName
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class PermissionTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LocationPermissionActivity::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun testLocationPermissionFiresProfileName() {
        init()

        val buttonContinue = Espresso.onView(ViewMatchers.withId(R.id.button))
        buttonContinue.perform(ViewActions.click())

        activityRule.scenario.onActivity { activity ->
            activity.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        }
        Thread.sleep(500)
        Intents.intended(IntentMatchers.hasComponent(ProfileName::class.java.name))
        release()
    }

}