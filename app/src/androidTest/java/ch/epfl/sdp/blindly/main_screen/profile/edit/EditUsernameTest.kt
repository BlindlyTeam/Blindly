package ch.epfl.sdp.blindly.main_screen.profile.edit

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.main_screen.profile.edit.EditProfile
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.storage.UserCache
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.containsString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

const val CORRECT_NAME = "Alice"
private const val INCORRECT_CHARS = "Ali;;;ce"
private const val INCORRECT_SHORT_NAME = "A"
private const val INCORRECT_LONG_NAME = "abcdefabcdefabcdefabcdef"
private const val ERROR_LONG_NAME = "Name can't be more than 20 characters!"
private const val ERROR_SHORT_NAME = "Name can't be less than 2 characters!"
private const val ERROR_CHARACTERS = "Please use only letters."

@HiltAndroidTest
class EditUsernameTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(EditProfile::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userHelper: UserHelper

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var userCache: UserCache

    @Inject
    lateinit var db: FirebaseFirestore

    @Before
    fun setup() {
        hiltRule.inject()
        val intent = Intent(ApplicationProvider.getApplicationContext(), EditUsername::class.java)
        ActivityScenario.launch<EditUsername>(intent)
    }

    @Test
    fun shortNameOutputsError() {
        onView(withId(R.id.edit_username))
            .perform(ViewActions.clearText(), ViewActions.typeText(INCORRECT_SHORT_NAME))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.update_username)).perform(click())
        onView(withId(R.id.warning1_p2)).check(
            matches(
                withText(
                    containsString(
                        ERROR_SHORT_NAME
                    )
                )
            )
        )
        onView(withId(R.id.warning1_p2)).check(
            matches(
                isDisplayed()
            )
        )
    }

    @Test
    fun longNameOutputsError() {
        onView(withId(R.id.edit_username))
            .perform(ViewActions.clearText(), ViewActions.typeText(INCORRECT_LONG_NAME))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.update_username)).perform(click())
        onView(withId(R.id.warning2_p2)).check(
            matches(
                withText(
                    containsString(
                        ERROR_LONG_NAME
                    )
                )
            )
        )
        onView(withId(R.id.warning2_p2)).check(
            matches(
                isDisplayed()
            )
        )
    }

    @Test
    fun incorrectCharsOutputError() {
        onView(withId(R.id.edit_username))
            .perform(ViewActions.clearText(), ViewActions.typeText(INCORRECT_CHARS))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.update_username)).perform(click())
        onView(withId(R.id.warning3_p2)).check(
            matches(
                withText(
                    containsString(
                        ERROR_CHARACTERS
                    )
                )
            )
        )
        onView(withId(R.id.warning3_p2)).check(
            matches(
                isDisplayed()
            )
        )
    }
}