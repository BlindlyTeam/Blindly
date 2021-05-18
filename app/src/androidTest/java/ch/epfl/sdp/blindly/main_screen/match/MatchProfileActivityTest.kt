package ch.epfl.sdp.blindly.main_screen.match

import android.location.Location
import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.fake_module.FakeUserHelperModule.Companion.TEST_UID
import ch.epfl.sdp.blindly.fake_module.FakeUserCacheModule.Companion.fakeUser
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.main_screen.chat.ChatActivity
import ch.epfl.sdp.blindly.main_screen.chat.match_profile.MatchProfileActivity
import ch.epfl.sdp.blindly.user.User
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MatchProfileActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(
        MatchProfileActivity::class.java,
        bundleOf(ChatActivity.MATCH_ID to TEST_UID)
    )

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
        Intents.init()
    }

    @After
    fun afterEach() {
        Intents.release()
    }

    @Test
    fun userNameAgeIsCorrectlyDisplayed() {
        val userAge = User.getAgeFromBirthday(fakeUser.birthday!!)

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