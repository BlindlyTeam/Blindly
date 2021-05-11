package ch.epfl.sdp.blindly.main_screen.profile.settings

import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.user.LOCATION
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.viewmodel.UserViewModel
import ch.epfl.sdp.blindly.viewmodel.ViewModelAssistedFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

val LAUSANNE_LATLNG = LatLng(46.5, 6.6)

/**
 * Activity that shows the position of the user on a map.
 */
@AndroidEntryPoint
class SettingsLocation : AppCompatActivity(), OnMapReadyCallback {

    @Inject
    lateinit var userHelper: UserHelper

    @Inject
    lateinit var assistedFactory: ViewModelAssistedFactory

    private lateinit var viewModel: UserViewModel

    private lateinit var mapView: MapView
    private var location: Location? = null

    private lateinit var userLocation: String
    private lateinit var currentLocation: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_location)
        instantiateViewModel()

        supportActionBar?.hide()

        val locSer = AndroidLocationService(this)
        location = locSer.getCurrentLocation()
        val locationText = findViewById<TextView>(R.id.my_current)
        //Passed as an intent this is the location of the user from firestore
        userLocation = intent.getStringExtra(EXTRA_LOCATION).toString()
        //This a freshly computed location
        currentLocation =
            location?.let { it1 ->
                AndroidLocationService.getCurrentLocationStringFromLocation(
                    this,
                    it1
                )
            }
                .toString()

        locationText.text = currentLocation


        mapView = findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val loc: LatLng = if (location != null) {
            LatLng(location!!.latitude, location!!.longitude)
        } else {
            LAUSANNE_LATLNG
        }
        googleMap.addMarker(MarkerOptions().position(loc).title("My position"))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(14f))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBackPressed() {
        if (currentLocation != userLocation && location != null) {
            viewModel.updateField(LOCATION, listOf(location!!.latitude, location!!.longitude))
        }
        super.onBackPressed()
    }

    private fun instantiateViewModel() {
        val bundle = Bundle()
        bundle.putString(UserHelper.EXTRA_UID, userHelper.getUserId())

        val viewModelFactory = assistedFactory.create(this, bundle)

        viewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]
    }
}