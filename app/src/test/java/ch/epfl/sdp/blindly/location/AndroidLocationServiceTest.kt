package ch.epfl.sdp.blindly.location

import android.location.Location
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AndroidLocationServiceTest {

    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val locationService = AndroidLocationService(context)

    @Test
    fun testGetCurrentLocation() {
       val location = locationService.getCurrentLocation()
        assertEquals(location!!.javaClass, Location::class.java)
    }

    @Test
    fun testUpdateLocation() {
        val loc1 = locationService.getCurrentLocation()
        val loc2 = locationService.onLocationChanged(loc1!!)
        assertEquals(loc1, loc2)

    }
}