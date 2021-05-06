package ch.epfl.sdp.blindly

import android.location.Location
import ch.epfl.sdp.blindly.helpers.BlindlyLatLng
import ch.epfl.sdp.blindly.location.AndroidLocationService.Companion.createLocationTableEPFL
import ch.epfl.sdp.blindly.settings.LAUSANNE_LATLNG
import ch.epfl.sdp.blindly.user.User
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BlindlyLatLngTest {
    private val LAT = LAUSANNE_LATLNG.latitude
    private val LNG = LAUSANNE_LATLNG.longitude
    @Test
    fun constructFromDouble() {
        val pos = BlindlyLatLng(LAT, LNG)
        assertEquals("lat matches", LAT, pos.getLatitude())
        assertEquals("lng matches", LNG, pos.getLongitude())
    }
    @Test
    fun constructFromGMapLatLng() {
        val pos = BlindlyLatLng(LAUSANNE_LATLNG)
        assertEquals("lat matches", LAT, pos.getLatitude())
        assertEquals("lng matches", LNG, pos.getLongitude())
    }
    @Test
    fun toLatLngWorks() {
        val pos = BlindlyLatLng(LAUSANNE_LATLNG).toLatLng()
        assertEquals("lat matches", LAT, pos!!.latitude, 0.0)
        assertEquals("lng matches", LNG, pos!!.longitude, 0.0)
    }
    @Test
    fun toLatLngNullWorks() {
        val pos = BlindlyLatLng().toLatLng()
        assertNull(pos)
    }
    @Test
    fun toLatLngLatNullWorks() {
        val pos = BlindlyLatLng(LAT, null).toLatLng()
        assertNull(pos)
    }
    @Test
    fun toLatLngLngNullWorks() {
        val pos = BlindlyLatLng(null, LNG).toLatLng()
        assertNull(pos)
    }

}
