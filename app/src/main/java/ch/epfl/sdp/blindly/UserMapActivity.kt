package ch.epfl.sdp.blindly

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.chat.ChatAdapter
import ch.epfl.sdp.blindly.helpers.BlindlyLatLng
import ch.epfl.sdp.blindly.helpers.DatatbaseHelper
import ch.epfl.sdp.blindly.helpers.Message
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.location.AndroidLocationService.Companion.getCurrentLocation
import ch.epfl.sdp.blindly.location.LocationService
import ch.epfl.sdp.blindly.permissions.LocationPermission.Companion.LOCATION_PERMISSION_REQUEST_CODE
import ch.epfl.sdp.blindly.permissions.LocationPermission.Companion.PermissionDeniedDialog.Companion.newInstance
import ch.epfl.sdp.blindly.permissions.LocationPermission.Companion.isPermissionGranted
import ch.epfl.sdp.blindly.permissions.LocationPermission.Companion.requestPermission
import ch.epfl.sdp.blindly.settings.LAUSANNE_LATLNG
import ch.epfl.sdp.blindly.user.UserHelper
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class PositionViewModel: ViewModel() {

    fun sendLocation(
        locationDatabase: DatatbaseHelper.LocationLiveDatabase,
        locationService: AndroidLocationService
    ) {
        // Create a new coroutine to move the execution off the UI thread
        viewModelScope.launch(Dispatchers.Main) {
            while (true) {
                val pos = locationService.getCurrentLocation()
                if (pos != null) {
                    val latLng: BlindlyLatLng = BlindlyLatLng(pos.latitude, pos.longitude)
                    locationDatabase.updateLocation(latLng)
                }
                delay(1000)
            }
        }
    }
}

/*
 * An activity displaying two users live
 */
@AndroidEntryPoint
class UserMapActivity: AppCompatActivity(), OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * [.onRequestPermissionsResult].
     */
    private var permissionDenied = false
    private lateinit var map: GoogleMap
    // FIXME add name and default position
    private lateinit var otherUserMarker: Marker;

    @Inject
    lateinit var databaseHelper: DatatbaseHelper
    private lateinit var locationDatabase: DatatbaseHelper.LocationLiveDatabase

    @Inject
    lateinit var userHelper: UserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_map_activity)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        // Cancel loading if we can't get the user id
        val currentUserId = userHelper.getUserId() ?: return
        val matchId = intent.extras?.getString("matchedId") ?: "default_user"

        locationDatabase = databaseHelper.getLocationLiveDatabase(currentUserId, matchId)
        val model: PositionViewModel by viewModels()
        model.sendLocation(locationDatabase, AndroidLocationService(applicationContext))
        locationDatabase.addListener(object :
            DatatbaseHelper.BlindlyLiveDatabase.EventListener<BlindlyLatLng>() {
            override fun onMessageReceived(message: Message<BlindlyLatLng>) {
                updateMarker(message)
            }
            override fun onMessageUpdated(message: Message<BlindlyLatLng>) {
                updateMarker(message)
            }
        })


    }

    private fun updateMarker(message: Message<BlindlyLatLng>) {
        if (message.currentUserId == userHelper.getUserId()) return
        val pos = message.messageText ?: return
        val latLng = pos.toLatLng() ?: return
        otherUserMarker.position = latLng
        // Always keep match position on the center of the map
        map!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return

        enableMyLocation()

        // Add markers from the intent
        otherUserMarker = map!!.addMarker(MarkerOptions().position(
            LAUSANNE_LATLNG).title("Your match"))
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        // [START maps_check_location_permission]
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        }
        // [END maps_check_location_permission]
    }


    // [START maps_check_location_permission_result]
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        if (isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation()
        } else {
            // Permission was denied. Display an error message
            // [START_EXCLUDE]
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true
            // [END_EXCLUDE]
        }
    }

    // [END maps_check_location_permission_result]
    override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private fun showMissingPermissionError() {
        newInstance(true).show(supportFragmentManager, "dialog")
    }


    companion object {
        public const val POINTS = "points";

    }

}