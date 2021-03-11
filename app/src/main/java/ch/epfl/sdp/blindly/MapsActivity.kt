package ch.epfl.sdp.blindly

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.location.LocationPermission
import ch.epfl.sdp.blindly.location.LocationService

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var location: Location? = null
    private lateinit var locSer: LocationService
    private var canLocate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locPer = LocationPermission()

        if(!locPer.permission) {
            locPer.checkAndRequestLocationPermission(this, this)
        }
        canLocate = locPer.permission
        if(canLocate) {
            locSer = AndroidLocationService(this)
            location = locSer.getCurrentLocation()
        }
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val loc: LatLng = if(location != null && canLocate) {
            LatLng(location!!.latitude, location!!.longitude)
        } else {
            LatLng(46.5, 6.6)
        }
        mMap.addMarker(MarkerOptions().position(loc).title("My position"))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(13f))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
    }
}