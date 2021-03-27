package ch.epfl.sdp.blindly.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.LocationPermissionActivity
import ch.epfl.sdp.blindly.R

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