package ch.epfl.sdp.blindly.splash_screen

import android.app.Activity
import android.app.Instrumentation
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.SplashScreen
import ch.epfl.sdp.blindly.main_screen.MainScreen
import ch.epfl.sdp.blindly.user.UserHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.fail
import org.junit.After
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

        init()

        // Block the intent opening the main screen so that we have time to do our checks here
        val instrumentation: Instrumentation = InstrumentationRegistry.getInstrumentation()
        val filter: IntentFilter? = null
        val monitor = instrumentation.addMonitor(filter, null, true)
        intended(anyIntent())
        instrumentation.removeMonitor(monitor)
    }

    @After
    fun afterEach() {
        release()
    }

    @Test
    fun splashScreenDisplaysSplashscreenDotPNG() {
        activityRule.scenario.onActivity { activity ->
            val imageView: ImageView = activity.findViewById(R.id.splashscreen_heart)
            val resIdImage: Int = R.drawable.splash_screen_foreground

            if (!isImageEqualToRes(imageView, resIdImage)) {
                fail("Expected to find splashscreen.png for splash_screen")
            }
        }
    }

    @Test
    fun ifUserIsLoggedInMainScreenStarts() {
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
}