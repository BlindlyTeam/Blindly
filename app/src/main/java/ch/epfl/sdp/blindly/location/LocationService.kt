package ch.epfl.sdp.blindly.location

import android.location.Location

interface LocationService {

    fun getCurrentLocation(): Location?
}