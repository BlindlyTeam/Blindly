package ch.epfl.sdp.blindly.main_screen.my_matches.match_profile

import android.content.Intent
import android.location.Location
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.fake_module.FakeUserRepositoryModule.Companion.TEST_UID
import ch.epfl.sdp.blindly.fake_module.FakeUserRepositoryModule.Companion.fakeUser
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.main_screen.my_matches.MyMatchesAdapter.Companion.BUNDLE_MATCHED_UID_LABEL
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.storage.UserCache
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MatchProfileActivityTest {
    private val intent = Intent(
        ApplicationProvider.getApplicationContext(),
        MatchProfileActivity::class.java
    ).putExtra(BUNDLE_MATCHED_UID_LABEL, TEST_UID)

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var userCache: UserCache

    @Inject
    lateinit var db: FirebaseFirestore

    @Inject
    lateinit var userHelper: UserHelper

    @get:Rule
    val activityRule = ActivityScenarioRule<MatchProfileActivity>(intent)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

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
    fun userNameAgeIsCorrectlyDisplayed() {
        val userAge = User.getUserAge(fakeUser)

        onView(withId(R.id.matchProfileNameAge)).check(
            matches(
                withText(
                    "${fakeUser.username}, " + "$userAge"
                )
            )
        )
    }

    @Test
    fun userGenderIsCorrectlyDisplayed() {
        onView(withId(R.id.matchProfileGender)).check(matches(withText("${fakeUser.gender}")))
    }

    @Test
    fun userLocationIsCorrectlyDisplayed() {
        val userLocation = Location("")
        userLocation.latitude = fakeUser.location!![0]
        userLocation.longitude = fakeUser.location!![1]
        var userLocationText = ""

        activityRule.scenario.onActivity {
            userLocationText =
                AndroidLocationService.getCurrentLocationStringFromLocation(it, userLocation)
        }

        onView(withId(R.id.matchProfileLocation)).check(matches(withText(userLocationText)))
    }

    @Test
    fun userSexualOrientationsAreCorrectlyDisplayed() {
        for (orientation in fakeUser.sexualOrientations!!) {
            onView(withId(R.id.matchProfileOrientations)).check(
                matches(
                    withChild(
                        withText(orientation)
                    )
                )
            )
        }
    }

    @Test
    fun userPassionsAreCorrectlyDisplayed() {
        for (passion in fakeUser.passions!!) {
            onView(withId(R.id.matchProfilePassions)).check(matches(withChild(withText(passion))))
        }
    }
}