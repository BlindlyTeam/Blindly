package ch.epfl.sdp.blindly.utils

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class CheckInternetTest {

    lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun isConnectedIsTrueIfConnected() {
        val bool: Boolean = CheckInternet.internetIsConnected(context)
        MatcherAssert.assertThat(bool, Matchers.equalTo(true))
    }
}