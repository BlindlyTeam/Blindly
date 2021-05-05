package ch.epfl.sdp.blindly.chat

import android.view.View
import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.TreeIterables
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.helpers.DatatbaseHelper
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


const val OTHER_USER_ID = "other_user_id"
const val TEST_MESSAGE = "This is a test message"

@HiltAndroidTest
class LiveChatTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(
        ChatActivity::class.java,
        bundleOf(ChatActivity.MATCH_ID to OTHER_USER_ID)
    )

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var user: UserHelper
    @Inject
    lateinit var liveDb: DatatbaseHelper

    @Before
    fun setup() {
        hiltRule.inject()
        init()
    }

    @After
    fun afterEach() {
        release()
    }

    private fun withViewCount(viewMatcher: Matcher<View>, expectedCount: Int): Matcher<View?>? {
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

            protected override fun matchesSafely(root: View?): Boolean {
                actualCount = 0
                val iterable = TreeIterables.breadthFirstViewTraversal(root)
                actualCount =
                    Iterables.size(Iterables.filter(iterable, withMatcherPredicate(viewMatcher)))
                return actualCount == expectedCount
            }
        }
    }

    private fun withMatcherPredicate(matcher: Matcher<View>): Predicate<View?>? {
        return Predicate<View?> { view -> matcher.matches(view) }
    }

    private fun sendMessage() {
        onView(withId(R.id.newMessageText))
            .perform(clearText(), typeText(TEST_MESSAGE))

        closeSoftKeyboard()
        val buttonUpdate = onView(withId(R.id.sendButton))
        buttonUpdate.check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
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
            buttonUpdate.check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            buttonUpdate.perform(click())
        }
        sendMessage()
        onView(withId(R.id.recyclerView)).check(matches(withViewCount(withText(TEST_MESSAGE), 2)))

    }
}