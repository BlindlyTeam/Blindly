package ch.epfl.sdp.blindly.main_screen.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.location.BlindlyLatLng
import ch.epfl.sdp.blindly.database.DatabaseHelper
import ch.epfl.sdp.blindly.main_screen.chat.Message
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.location.permissions.LocationPermission.Companion.LOCATION_PERMISSION_REQUEST_CODE
import ch.epfl.sdp.blindly.location.permissions.LocationPermission.Companion.PermissionDeniedDialog.Companion.newInstance
import ch.epfl.sdp.blindly.location.permissions.LocationPermission.Companion.isPermissionGranted
import ch.epfl.sdp.blindly.location.permissions.LocationPermission.Companion.requestPermission
import ch.epfl.sdp.blindly.main_screen.profile.settings.LAUSANNE_LATLNG
import ch.epfl.sdp.blindly.user.UserHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/*
 * An activity displaying two users live
 */
@AndroidEntryPoint
class UserMapActivity : AppCompatActivity(), OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * [onRequestPermissionsResult].
     */
    private var permissionDenied = false

    private lateinit var map: GoogleMap
    private var otherUserMarker: Marker? = null
    private lateinit var locationDatabase: DatabaseHelper.LocationLiveDatabase
    private lateinit var matchName: String

    @Inject
    lateinit var databaseHelper: DatabaseHelper

    @Inject
    lateinit var userHelper: UserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        // Cancel loading if we can't get the user id
        val currentUserId = userHelper.getUserId() ?: return
        val matchId = intent.extras?.getString(MATCH_ID) ?: DEFAULT_MATCH_ID
        matchName =
            intent.extras?.getString(MATCH_NAME) ?: getString(R.string.default_label_map_pin)

        locationDatabase = databaseHelper.getLocationLiveDatabase(currentUserId, matchId)
        locationDatabase.addListener(object :
            DatabaseHelper.BlindlyLiveDatabase.EventListener<BlindlyLatLng>() {
            override fun onMessageReceived(message: Message<BlindlyLatLng>) {
                updateMarker(message)
            }

            override fun onMessageUpdated(message: Message<BlindlyLatLng>) {
                updateMarker(message)
            }
        })

        AndroidLocationService(applicationContext).addLocationChangeListener(object :
            AndroidLocationService.LocationChangeListener() {
            override fun onLocationChange(pos: BlindlyLatLng) {
                locationDatabase.updateLocation(pos)
            }
        })
    }

    private fun updateMarker(message: Message<BlindlyLatLng>) {
        if (!::map.isInitialized) return
        if (message.currentUserId == userHelper.getUserId()) return
        val pos = message.messageText ?: return
        val latLng = pos.toLatLng() ?: return
        tryToAddMarker(latLng)?.position = latLng
    }

    /**
     * Sometimes google map can fail to add the marker so we
     * retry everytime we need it
     *
     * @return Hopefully the marker
     */
    private fun tryToAddMarker(latLng: LatLng): Marker? {
        if (otherUserMarker == null && ::map.isInitialized) {
            otherUserMarker = map.addMarker(
                MarkerOptions()
                    .position(LAUSANNE_LATLNG)
                    .title(matchName)
            )
            // When we first have the match position,
            // center the map to their position
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        }
        return otherUserMarker
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
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        // [START maps_check_location_permission]
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            requestPermission(
                this, LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        }
        // [END maps_check_location_permission]
    }

    // [START maps_check_location_permission_result]
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        if (isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
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
        const val MATCH_ID = "matchedId"
        const val MATCH_NAME = "matchedName"
        const val DEFAULT_MATCH_ID = "default_user"
    }
}