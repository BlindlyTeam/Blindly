package ch.epfl.sdp.blindly.location

import android.location.Criteria
import android.location.LocationManager
import android.location.LocationProvider

data class AndroidLocationManager(val locationManager: LocationManager, val locationProvider: LocationProvider, val locationCriteria: Criteria): LocationService {

    override fun getCurrentLocation() {
    }
}