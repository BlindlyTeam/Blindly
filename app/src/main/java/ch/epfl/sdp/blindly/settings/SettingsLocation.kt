package ch.epfl.sdp.blindly.settings

import android.location.Location
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.location.LocationService
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class SettingsLocation : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private var location: Location? = null
    private lateinit var locSer: LocationService
    private val LAUSANNE = LatLng(46.5, 6.6)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_location)

        locSer = AndroidLocationService(this)
        location = locSer.getCurrentLocation()

        val currentLocation = intent.getStringExtra(EXTRA_LOCATION)
        findViewById<TextView>(R.id.my_current).apply {
            text = currentLocation
        }

        supportActionBar?.hide()

        mapView = findViewById(R.id.map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        val loc: LatLng = if (location != null) {
            LatLng(location!!.latitude, location!!.longitude)
        } else {
            LAUSANNE
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
}