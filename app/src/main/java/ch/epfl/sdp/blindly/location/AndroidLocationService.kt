package ch.epfl.sdp.blindly.location

import android.content.Context
import android.location.Location
import android.location.LocationManager

private const val MIN_TIME_FOR_UPDATE = 1L
private const val MIN_DISTANCE_FOR_UPDTAE = 1F

/**
 * The purpose of this class is to get the position of the user
 *
 * @property context
 */
class AndroidLocationService(private var context: Context) : LocationService {

    private var isGPSEnable = false
    private var isNetworkEnable = false
    private var location: Location? = null

    private lateinit var locationManager: LocationManager

    init {
        getCurrentLocation()
    }

    /**
     * Get the current location of the user
     * Get the location from the GPS and if disabled from the network
     *
     * @return the users location or null if GPS and network are disable
     */
    override fun getCurrentLocation(): Location? {
        try {
            //Get the location manager
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            //Check if the GPS is enabled
            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            //Check if the network is enabled
            isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if ((isGPSEnable) || (isNetworkEnable)) {
                location = if (isGPSEnable) {
                    //Get location from GPS
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_FOR_UPDATE,
                        MIN_DISTANCE_FOR_UPDTAE,
                        this
                    )
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                } else {
                    //get location from network
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_FOR_UPDATE,
                        MIN_DISTANCE_FOR_UPDTAE,
                        this
                    )
                    locationManager.getLastKnownLocation((LocationManager.NETWORK_PROVIDER))
                }
            }
        } catch (e: SecurityException) {
            throw e
        }
        return location
    }

    /**
     * Change the user's location if the previous one is different from the actual one
     *
     * @param loc the user's previous location
     */
    override fun onLocationChanged(loc: Location) {
        location = getCurrentLocation()
    }

}
