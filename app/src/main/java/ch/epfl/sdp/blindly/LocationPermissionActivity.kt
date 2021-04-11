package ch.epfl.sdp.blindly

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import ch.epfl.sdp.blindly.profile_setup.ProfileName
import ch.epfl.sdp.blindly.utils.LocationPermission

class LocationPermissionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_permission)

        val button: Button = findViewById(R.id.button)

        button.setOnClickListener {
            LocationPermission.requestPermission(
                this, LocationPermission.LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        }
    }
    // [START maps_check_location_permission_result]
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != LocationPermission.LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        if (LocationPermission.isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            val intent = Intent(
                this, ProfileName::class.java)
            startActivity(intent)
        } else {
            // Permission was denied. Display an error message
            // [START_EXCLUDE]
            LocationPermission.Companion.PermissionDeniedDialog.newInstance(true)
                .show(supportFragmentManager, "dialog")
            // [END_EXCLUDE]
        }
    }

}