package ch.epfl.sdp.blindly.profile_edit

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.user.GENDER
import ch.epfl.sdp.blindly.user.PASSIONS
import ch.epfl.sdp.blindly.user.SEXUAL_ORIENTATIONS
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.storage.UserCache
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class EditProfileTest {

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
        init()
    }

    @After
    fun afterEach() {
        release()
    }

    @Test
    fun clickingOnUsernameFiresEditUsername() {
        onView(withId(R.id.username_button)).perform(click())
        intended(hasComponent(EditUsername::class.java.name))
    }

    @Test
    fun clickingOnGenderFiresEditGender() {
        val TEST_GENDER = "Woman"
        onView(withId(R.id.gender_button)).perform(click())
        intended(
            allOf(
                hasComponent(EditGender::class.java.name),
                hasExtra(GENDER, TEST_GENDER)
            )
        )
    }

    @Test
    fun clickingOnSexualOrientationsFiresEditSexualOrientations() {
        val TEST_SEXUAL_ORIENTATIONS = arrayListOf(GAY, LESBIAN)
        onView(withId(R.id.sexual_orientations_button)).perform(click())
        intended(
            allOf(
                hasComponent(EditSexualOrientations::class.java.name),
                hasExtra(SEXUAL_ORIENTATIONS, TEST_SEXUAL_ORIENTATIONS)
            )
        )
    }

    @Test
    fun clickingOnPassionsFiresEditPassions() {
        val TEST_PASSIONS = arrayListOf(TEA, COFFEE)
        onView(withId(R.id.passions_button)).perform(click())
        intended(
            allOf(
                hasComponent(EditPassions::class.java.name),
                hasExtra(PASSIONS, TEST_PASSIONS)
            )
        )
    }
}