package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.main_screen.MainScreen
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.UserHelper
import javax.inject.Inject

class ProfileFinished : AppCompatActivity() {
    @Inject
    lateinit var user: UserHelper

    private lateinit var userBuilder: User.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_finished)
    }

    /**
     * Starts main screen activity
     *
     * @param v the current view
     */
    fun startMainScreen(v: View) {
        setUser()
        val intent = Intent(this, MainScreen::class.java)
        startActivity(intent)
    }

    private fun setUser() {
        val location = getCurrentAddress()
        userBuilder.setLocation(location)
        user.setUserProfile(userBuilder)
    }

    private fun getCurrentAddress(): String {
        val currentLocation = AndroidLocationService(this).getCurrentLocation()
        val latitude = currentLocation?.latitude
        val longitude = currentLocation?.longitude
        val geocoder = Geocoder(this)
        if (latitude != null && longitude != null) {
            val address = geocoder.getFromLocation(latitude, longitude, 5)
            val country = address[0].countryName
            val city = address[0].locality
            return "$city, $country"
        }
        return "Location not found"
    }

}