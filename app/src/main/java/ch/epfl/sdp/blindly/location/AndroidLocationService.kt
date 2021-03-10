package ch.epfl.sdp.blindly.location

import android.location.Location
import android.content.Context
import android.location.Criteria
import android.location.LocationManager

data class AndroidLocationService(val locationManager: LocationManager, val locationProvider: String?, val locationCriteria: Criteria?): LocationService {

    fun buildManagerFromContext(context: Context): LocationManager {
        return context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }


    fun buildFromContextAndProvider(context: Context, provider: String): LocationService {
        return AndroidLocationService(buildManagerFromContext(context), provider, null)
    }

    fun buildFromContextAndCriteria(context: Context, criteria: Criteria): LocationService {
        return AndroidLocationService(buildManagerFromContext(context), null, criteria)
    }

    override fun getCurrentLocation(): Location? {
        try {
            if(locationProvider != null) {
                val loc: Location? = this.locationManager.getLastKnownLocation(locationProvider)
                if (loc != null)
                    return loc
            }
            return null
        } catch(e: SecurityException) {
            throw e
        }
    }
}