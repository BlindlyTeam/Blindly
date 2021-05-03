package ch.epfl.sdp.blindly.location

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import ch.epfl.sdp.blindly.user.User

private const val MIN_TIME_FOR_UPDATE = 1L
private const val MIN_DISTANCE_FOR_UPDTAE = 1F
private const val EPFL_LAT = 46.5
private const val EPFL_LONG = 6.5

/**
 * The purpose of this class is to get the position of the user
 *
 * @property context
 */
class AndroidLocationService(private var context: Context) : LocationService {

    private var isGPSEnable = false
    private var isNetworkEnable = false
    private var canGetLocation = false
    private var location: Location? = null

    private lateinit var locationManager: LocationManager

    init {
        getCurrentLocation()
    }

    /**
     * Get the current location of the user
     * Get the location from the GPS and if disabled from the network
     *
     * @return the user's location or null if GPS and network are disabled
     */
    override fun getCurrentLocation(): Location? {
        try {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            //check for GPS
            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            //check for network
            isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if ((isGPSEnable) || (isNetworkEnable)) {
                canGetLocation = true
                location = if (isGPSEnable) {
                    //get location from GPS
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_FOR_UPDATE,
                        MIN_DISTANCE_FOR_UPDTAE,
                        this
                    )
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                } else {
                    //get location from Network
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

    companion object {
        fun createLocationTableEPFL(): List<Double> {
            return listOf(EPFL_LAT, EPFL_LONG)
        }

        /**
         * Compute the user's location, either get the most recent one or take the one stored
         * in the user
         *
         * @param context the context
         * @param user the user
         * @return the user's location
         */
        fun getCurrentLocation(context: Context, user: User?): String {
            val currentLocation = AndroidLocationService(context).getCurrentLocation()
            val latitude = currentLocation?.latitude
            val longitude = currentLocation?.longitude
            val geocoder = Geocoder(context)
            //If the current location is available
            if (latitude != null && longitude != null) {
                return toAddress(geocoder, latitude, longitude)
            }
            //Else take the location stored in the database
            if (user != null) {
                val lat = user.location?.get(0)
                val lon = user.location?.get(1)
                if (lat != null && lon != null) {
                    return toAddress(geocoder, lat, lon)
                }
            }
            return "Location not found"
        }

        private fun toAddress(geocoder: Geocoder, latitude: Double, longitude: Double): String {
            val address = geocoder.getFromLocation(latitude, longitude, 5)
            val country = address[0].countryName
            val city = address[0].locality
            return "$city, $country"
        }
    }
}
