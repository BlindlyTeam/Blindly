package ch.epfl.sdp.blindly

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindly.main_screen.MainScreen
import ch.epfl.sdp.blindly.user.UserHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.io.ByteArrayOutputStream
import javax.inject.Inject


@HiltAndroidTest
class SplashScreenActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(SplashScreen::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var user: UserHelper

    @Before
    fun setup() {
        hiltRule.inject()
    }

    private fun isImageEqualToRes(actualImageView: ImageView, expectedDrawable: Int): Boolean {
        var activity: Activity? = null
        activityRule.scenario.onActivity { a ->
            activity = a
        }

        val expected: Drawable? = activity?.resources?.getDrawable(
            expectedDrawable
        )
        val actual: Drawable = actualImageView.drawable
        if (expected != null) {
            val expectedBitmap: Bitmap = getBitmapOfDrawable(expected)
            val actualBitmap: Bitmap = getBitmapOfDrawable(actual)
            return areBitmapsEqual(expectedBitmap, actualBitmap)
        }
        return false
    }

    private fun getBitmapOfDrawable(drawable: Drawable): Bitmap {
        val bd = drawable as BitmapDrawable
        return bd.bitmap
    }

    private fun areBitmapsEqual(bitmap1: Bitmap, bitmap2: Bitmap): Boolean {
        val array1 = bitmapToByteArray(bitmap1)
        val array2 = bitmapToByteArray(bitmap2)
        return array1.contentEquals(array2)
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val bos = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.PNG, 100, bos)
        return bos.toByteArray()
    }

    @Test
    fun splashScreenDisplaysSplashscreenDotPNG() {
        init()
        var imageView: ImageView? = null
        activityRule.scenario.onActivity { activity ->
            imageView = activity.findViewById(R.id.splashscreen_heart)
        }
        val resIdImage: Int = R.drawable.splash_screen_foreground

        if (!imageView?.let { isImageEqualToRes(it, resIdImage) }!!) {
            fail("Expected to find splashscreen.png for splash_screen")
        }
        release()
    }

    //TODO "Test running failed: INSTRUMENTATION_ABORTED: System has crashed."
    // The hasComponent() is wrong since there's no class.name to match with
    /*
    @Test
    fun ifUserIsNotLoggedInLoginStarts() {
        init()
        val intent = user.getSignInIntent()
        Mockito.`when`(user.isLoggedIn()).thenReturn(false)
        activityRule.scenario.onActivity { activity ->
            if (activity.isFinishing) {
                intended(
                    hasComponent(
                        intent.toString()
                    )
                )
            }
        }
        release()
    }

     */

    @Test
    fun ifUserIsLoggedInMainScreenStarts() {
        init()
        Mockito.`when`(user.isLoggedIn()).thenReturn(true)
        activityRule.scenario.onActivity { activity ->
            if (activity.isFinishing) {
                intended(
                    hasComponent(
                        MainScreen::class.java.name
                    )
                )
            }
        }
        release()
    }

    //TODO
    // The hasComponent() is wrong since there's no class.name to match with
    /*
    @Test
    fun onActivityResultFiresHouseRuleIfIsNewUser() {
        val intent = user.getSignInIntent()
        intending(hasComponent(intent.toString())).respondWith(
            Instrumentation.ActivityResult(
                RESULT_OK,
                null
                //Intent { (has extras) }
            )
        )
        activityRule.scenario.onActivity { activity ->
            activity.startActivityForResult(intent, UserHelper.RC_SIGN_IN)
        }

    }

    @Test
    fun onActivityResultFiresMaiScreenIfNotIsNewUser() {

    }

     */

}