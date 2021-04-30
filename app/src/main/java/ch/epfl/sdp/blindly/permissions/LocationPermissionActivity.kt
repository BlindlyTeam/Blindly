package ch.epfl.sdp.blindly.permissions

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.profile_setup.ProfileName
import ch.epfl.sdp.blindly.permissions.LocationPermission

private const val FINE_LOCATION_PERMISSION_CODE = 2

/**
 * Page to ask user to enable location. If allowed Profile Setup pages show up in order.
 */
class LocationPermissionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_permission)

        val button: Button = findViewById(R.id.enable_location_button)

        /**
         * Clicking on the button requires the location service to be able
         * to continue with the profile setup
         */
        button.setOnClickListener {
            LocationPermission.requestPermission(
                this, LocationPermission.LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        }
    }
    // [START maps_check_location_permission_result]
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
                this, ProfileName::class.java
            )
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
