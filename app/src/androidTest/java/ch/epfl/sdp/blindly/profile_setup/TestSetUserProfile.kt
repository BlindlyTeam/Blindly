package ch.epfl.sdp.blindly.profile_setup

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.utils.UserHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

//@HiltAndroidTest
class TestSetUserProfile {

    @get:Rule
    val activityRule = ActivityScenarioRule(ProfilePassions::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var user: UserHelper

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun userMetasAreSetCorectly() {
        Intents.init()
        onView(withId(R.id.button_p7)).perform(click())

        Intents.release()
    }

}