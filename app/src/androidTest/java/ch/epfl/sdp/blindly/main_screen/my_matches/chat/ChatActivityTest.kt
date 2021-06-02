package ch.epfl.sdp.blindly.main_screen.my_matches.chat

import android.content.Intent
import android.view.View
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.TreeIterables
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.database.DatabaseHelper
import ch.epfl.sdp.blindly.fake_module.FakeUserRepositoryModule.Companion.TEST_UID
import ch.epfl.sdp.blindly.fake_module.FakeUserRepositoryModule.Companion.fakeUser
import ch.epfl.sdp.blindly.main_screen.my_matches.MyMatchesAdapter.Companion.BUNDLE_MATCHED_UID_LABEL
import ch.epfl.sdp.blindly.main_screen.my_matches.MyMatchesAdapter.Companion.BUNDLE_MATCHED_USERNAME_LABEL
import ch.epfl.sdp.blindly.main_screen.my_matches.match_profile.MatchProfileActivity
import ch.epfl.sdp.blindly.user.UserHelper
import com.google.common.base.Predicate
import com.google.common.collect.Iterables
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

const val TEST_MESSAGE = "This is a test message"

@HiltAndroidTest
class ChatActivityTest {
    private val intent = Intent(
        ApplicationProvider.getApplicationContext(),
        ChatActivity::class.java
    ).putExtra(BUNDLE_MATCHED_UID_LABEL, TEST_UID)
        .putExtra(BUNDLE_MATCHED_USERNAME_LABEL, fakeUser.username)

    @get:Rule
    val activityRule = ActivityScenarioRule<ChatActivity>(intent)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var user: UserHelper

    @Inject
    lateinit var liveDb: DatabaseHelper

    @Before
    fun setup() {
        hiltRule.inject()
        init()
    }

    @After
    fun afterEach() {
        release()
    }

    private fun withViewCount(viewMatcher: Matcher<View>, expectedCount: Int): Matcher<View?> {
        return object : TypeSafeMatcher<View?>() {
            var actualCount = -1
            override fun describeTo(description: Description) {
                if (actualCount >= 0) {
                    description.appendText("With expected number of items: $expectedCount")
                    description.appendText("\n With matcher: ")
                    viewMatcher.describeTo(description)
                    description.appendText("\n But got: $actualCount")
                }
            }

            override fun matchesSafely(root: View?): Boolean {
                actualCount = 0
                val iterable = TreeIterables.breadthFirstViewTraversal(root)
                actualCount =
                    Iterables.size(Iterables.filter(iterable, withMatcherPredicate(viewMatcher)))
                return actualCount == expectedCount
            }
        }
    }

    private fun withMatcherPredicate(matcher: Matcher<View>): Predicate<View?> {
        return Predicate<View?> { view -> matcher.matches(view) }
    }

    private fun sendMessage() {
        onView(withId(R.id.newMessageText))
            .perform(clearText(), typeText(TEST_MESSAGE))

        closeSoftKeyboard()
        val buttonUpdate = onView(withId(R.id.sendButton))
        buttonUpdate.check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        buttonUpdate.perform(click())
    }

    @Test
    fun sendReceiveWorks() {
        sendMessage()
        onView(withId(R.id.recyclerView)).check(matches(withViewCount(withText(TEST_MESSAGE), 2)))
    }

    @Test
    fun sendReceiveMultipleMessagesWorks() {
        for (i in 0..1) {
            sendMessage()
        }
        onView(withId(R.id.recyclerView)).check(matches(withViewCount(withText(TEST_MESSAGE), 4)))
    }

    @Test
    fun sendingEmptyMessageDoesntDoAnything() {
        for (i in 0..3) {
            val buttonUpdate = onView(withId(R.id.sendButton))
            buttonUpdate.check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
            buttonUpdate.perform(click())
        }
        sendMessage()
        onView(withId(R.id.recyclerView)).check(matches(withViewCount(withText(TEST_MESSAGE), 2)))
    }

    @Test
    fun userNameIsCorrectlyDisplayedInActionBar() {
        onView(withId(R.id.matchNameBar)).check(matches(hasDescendant(withText(fakeUser.username))))
    }

    @Test
    fun clickingOnActionBarFiresMatchProfile() {
        onView(withId(R.id.matchNameBar)).perform(click())
        intended(hasComponent(MatchProfileActivity::class.java.name))
    }
}