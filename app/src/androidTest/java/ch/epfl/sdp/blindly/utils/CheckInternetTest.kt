package ch.epfl.sdp.blindly.utils

import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test


@HiltAndroidTest
class CheckInternetTest {

    @Test
    fun isConnectedIsTrueIfConnected() {
        val bool: Boolean = CheckInternet.internetIsConnected()
        MatcherAssert.assertThat(bool, Matchers.equalTo(true))
    }
}