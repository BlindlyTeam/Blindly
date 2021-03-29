package ch.epfl.sdp.blindly.location

import android.location.Location
import android.location.LocationListener

interface LocationService : LocationListener {

    fun getCurrentLocation(): Location?
}