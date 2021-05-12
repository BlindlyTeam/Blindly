package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.location.permissions.LocationPermissionActivity

/**
 * Shows the House Rules and starts LocationPermissionActivity when the button is pressed.
 */
class ProfileHouseRules : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_house_rules)
    }

    fun startPermissionActivity(view: View) {
        val intent = Intent(this, LocationPermissionActivity::class.java)
        startActivity(intent)
    }
}