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
import androidx.test.rule.GrantPermissionRule
import ch.epfl.sdp.blindly.profile_setup.ProfileName
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import android.Manifest;



@HiltAndroidTest
class PermissionTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LocationPermissionActivity::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION)

    @Test
    fun testLocationPermissionFiresProfileName() {
        init()

        val buttonContinue = Espresso.onView(ViewMatchers.withId(R.id.button))
        buttonContinue.perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(ProfileName::class.java.name))
        release()
    }

}