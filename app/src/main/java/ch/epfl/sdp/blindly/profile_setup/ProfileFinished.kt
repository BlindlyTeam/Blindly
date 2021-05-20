package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.database.localDB.AppDatabase
import ch.epfl.sdp.blindly.database.localDB.UserDAO
import ch.epfl.sdp.blindly.database.localDB.UserEntity
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.main_screen.MainScreen
import ch.epfl.sdp.blindly.main_screen.profile.settings.LAUSANNE_LATLNG
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

    @Inject
    lateinit var localDB: AppDatabase

    @Inject
    lateinit var userDAO: UserDAO

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
        val uid = user.getUserId()
        if (uid != null) {
            val birthday = userBuilder.birthday
            val age = birthday?.let { User.getAgeFromBirthday(it) }
            val minAge =
                if (age!! >= MAJORITY_AGE + UserHelper.DEFAULT_RANGE)
                    age - UserHelper.DEFAULT_RANGE
                else MAJORITY_AGE
            val maxAge = age + UserHelper.DEFAULT_RANGE

            val newUser = userBuilder.setRadius(UserHelper.DEFAULT_RADIUS)
                .setMatches(listOf())
                .setAgeRange(listOf(minAge, maxAge))
                .build()
            userDAO.insertUser(UserEntity(uid, newUser))
        }
    }

    private fun getCurrentLocation(): Location? {
        return AndroidLocationService(this).getCurrentLocation()
    }
}