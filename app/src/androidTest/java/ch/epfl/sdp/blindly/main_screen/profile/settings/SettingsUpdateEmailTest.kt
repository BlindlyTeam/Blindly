package ch.epfl.sdp.blindly.main_screen.profile.settings

import android.os.Handler
import android.os.Looper
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.release
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.fake_module.FakeUserHelperModule
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.main_screen.profile.settings.SettingsUpdateEmail
import ch.epfl.sdp.blindly.user.UserHelper
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

@HiltAndroidTest
class SettingsUpdateEmailTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SettingsUpdateEmail::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var user: UserHelper

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
    fun emailIsCorrect() {
        onView(withId(R.id.update_email_field)).check(
            ViewAssertions.matches(
                withHint(
                    user.getEmail()
                )
            )
        )
    }

    @Test
    fun emailUpdateWorks() {
        onView(withId(R.id.update_email_field))
            .perform(clearText(), typeText(FakeUserHelperModule.SECOND_EMAIL))

        closeSoftKeyboard()
        val buttonUpdate = onView(withId(R.id.update_email_button))
        buttonUpdate.perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.update_email_success_notice)).check(
            ViewAssertions.matches(
                Matchers.allOf(
                    withText(R.string.email_update_success),
                    withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
                )
            )
        )
    }

    private fun emailUpdateErrorIsHandled(e: Exception, expectedResId: Int) {
        // We can't change the things in onCreate, however things happening on button clicks it's fine
        // Fail email update with FirebaseAuthInvalidCredentialsException
        val taskCompletionSource = TaskCompletionSource<Void>()
        Handler(Looper.getMainLooper()).postDelayed({ taskCompletionSource.setException(e) }, 1000L)
        val successfulTask = taskCompletionSource.task
        Mockito.`when`(user.setEmail(FakeUserHelperModule.SECOND_EMAIL)).thenReturn(successfulTask)

        onView(withId(R.id.update_email_field))
            .perform(clearText(), typeText(FakeUserHelperModule.SECOND_EMAIL))

        closeSoftKeyboard()
        val buttonUpdate = onView(withId(R.id.update_email_button))
        buttonUpdate.perform(click())
        onView(withId(R.id.update_email_failure_notice)).check(
            ViewAssertions.matches(
                Matchers.allOf(
                    withText(expectedResId),
                    withEffectiveVisibility(Visibility.VISIBLE)
                )
            )
        )
    }

    @Test
    fun emailUpdateInvalidEmail() {
        emailUpdateErrorIsHandled(
            FirebaseAuthInvalidCredentialsException("aaa", "bbb"),
            R.string.invalid_email
        )
    }

    @Test
    fun emailUpdateUserCollision() {
        emailUpdateErrorIsHandled(
            FirebaseAuthUserCollisionException("aaa", "bbb"),
            R.string.email_taken
        )
    }

    @Test
    fun emailUpdateInvalidUser() {
        emailUpdateErrorIsHandled(
            FirebaseAuthInvalidUserException("aaa", "bbb"),
            R.string.try_to_logout
        )
    }

    @Test
    fun emailUpdateNeedRecentLogin() {
        emailUpdateErrorIsHandled(
            FirebaseAuthRecentLoginRequiredException("aaa", "bbb"),
            R.string.try_to_logout
        )
    }

    @Test
    fun emailUpdateOtherError() {
        emailUpdateErrorIsHandled(
            java.lang.Exception("aaa"),
            R.string.update_email_unknown_error
        )
    }
}