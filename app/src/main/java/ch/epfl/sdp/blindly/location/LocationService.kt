package ch.epfl.sdp.blindly.location

import android.location.Location
import android.location.LocationListener

/**
 * Interface to get the user's location
 */
interface LocationService : LocationListener {

    /**
     * Get the user's actual location
     *
     * @return the user's location
     */
    fun getCurrentLocation(): Location?
}