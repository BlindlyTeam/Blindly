package ch.epfl.sdp.blindly

import ch.epfl.sdp.blindly.location.BlindlyLatLng
import ch.epfl.sdp.blindly.main_screen.profile.settings.LAUSANNE_LATLNG
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Test

class BlindlyLatLngTest {
    companion object {
        private val LAT = LAUSANNE_LATLNG.latitude
        private val LNG = LAUSANNE_LATLNG.longitude
    }

    @Test
    fun constructFromDouble() {
        val pos = BlindlyLatLng(LAT, LNG)
        assertThat("lat matches", pos.getLatitude(), equalTo(LAT))
        assertThat("lng matches", pos.getLongitude(), equalTo(LNG))
    }
    @Test
    fun constructFromGMapLatLng() {
        val pos = BlindlyLatLng(LAUSANNE_LATLNG)
        assertThat("lat matches", pos.getLatitude(), equalTo(LAT))
        assertThat("lng matches", pos.getLongitude(), equalTo(LNG))
    }
    @Test
    fun toLatLngWorks() {
        val pos = BlindlyLatLng(LAUSANNE_LATLNG).toLatLng()!!
        assertThat("lat matches", pos.latitude, equalTo(LAT))
        assertThat("lng matches", pos.longitude, equalTo(LNG))
    }
    @Test
    fun toLatLngNullWorks() {
        val pos = BlindlyLatLng().toLatLng()
        assertThat(pos, nullValue())
    }
    @Test
    fun toLatLngLatNullWorks() {
        val pos = BlindlyLatLng(LAT, null).toLatLng()
        assertThat(pos, nullValue())
    }
    @Test
    fun toLatLngLngNullWorks() {
        val pos = BlindlyLatLng(null, LNG).toLatLng()
        assertThat(pos, nullValue())
    }

}
