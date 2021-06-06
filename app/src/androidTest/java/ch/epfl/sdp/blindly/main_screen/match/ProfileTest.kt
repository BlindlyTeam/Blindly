package ch.epfl.sdp.blindly.main_screen.match

import android.os.Parcel
import ch.epfl.sdp.blindly.main_screen.match.cards.Profile
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

@HiltAndroidTest
class ProfileTest {
    private val andre1 = Profile("UID1", "André", 25, "Man", 42, "PathAndré")

    @Test
    fun testProfileParcel1() {
        val parcel = Parcel.obtain()
        andre1.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)
        val test = Profile.CREATOR.createFromParcel(parcel)
        assertThat(test, equalTo(andre1))
    }

    @Test
    fun testProfileParcel2() {
        assertThat(andre1.describeContents(), equalTo(0))
    }
}