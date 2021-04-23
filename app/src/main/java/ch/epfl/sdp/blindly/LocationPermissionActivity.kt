package ch.epfl.sdp.blindly

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import ch.epfl.sdp.blindly.profile_setup.ProfileName

private const val FINE_LOCATION_PERMISSION_CODE = 2

/**
 * Page to ask user to enable location. If allowed Profile Setup pages show up in order.
 */
class LocationPermissionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_permission)

        val button: Button = findViewById(R.id.button)

        /**
         * Clicking on the button requires the location service to be able
         * to continue with the profile setup
         */
        button.setOnClickListener {
            do {
                var permitted = ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    FINE_LOCATION_PERMISSION_CODE
                )
            } while (!permitted)

            val intent = Intent(
                this, ProfileName::class.java
            )
            startActivity(intent)
        }
    }
}
