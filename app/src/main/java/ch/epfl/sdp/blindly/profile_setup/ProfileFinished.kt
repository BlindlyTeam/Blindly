package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.main_screen.MainScreen
import ch.epfl.sdp.blindly.settings.LAUSANNE_LATLNG
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.UserHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Activity that tells the user that the profile setup is finished, and sets everything up
 * on the database.
 */
@AndroidEntryPoint
class ProfileFinished : AppCompatActivity() {

    @Inject
    lateinit var user: UserHelper

    private lateinit var userBuilder: User.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_finished)

        val bundle = intent.extras
        userBuilder = bundle?.getString(EXTRA_USER)?.let { Json.decodeFromString(it) }!!
    }

    /**
     * Starts main screen activity
     *
     * @param v the current view
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun startMainScreen(v: View) {
        setUser()
        val intent = Intent(this, MainScreen::class.java)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUser() {
        val location = getCurrentLocation()
        userBuilder.setLocation(
            listOf(
                location?.latitude ?: LAUSANNE_LATLNG.latitude,
                location?.longitude ?: LAUSANNE_LATLNG.longitude
            )
        )
        user.setUserProfile(userBuilder)
    }

    private fun getCurrentLocation(): Location? {
        return AndroidLocationService(this).getCurrentLocation()
    }
}