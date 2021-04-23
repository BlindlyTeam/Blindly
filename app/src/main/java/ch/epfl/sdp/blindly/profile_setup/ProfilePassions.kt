package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.UserHelper
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

private const val SELECTION_LIMIT = 5

@AndroidEntryPoint
class ProfilePassions : AppCompatActivity() {
    @Inject
    lateinit var user: UserHelper
    lateinit var userBuilder: User.Builder
    private val passions: ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_passions)

        val bundle = intent.extras
        userBuilder = bundle?.getString(EXTRA_USER)?.let { Json.decodeFromString(it) }!!
    }

    /**
     * Checks the number of selected chip buttons by the user and passes them to the builder via
     * helper function getCheckedChip and starts ProfileAudioRecording if the number of selected
     * chips is within limits, if not outputs error.
     *
     * @param view the current view
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun startProfileAudioRecording(view: View) {
        findViewById<TextView>(R.id.warning_p7_1).visibility = View.INVISIBLE
        findViewById<TextView>(R.id.warning_p7_2).visibility = View.INVISIBLE

        val chipGroup = findViewById<ChipGroup>(R.id.chipGroup_p7)
        val ids = chipGroup.checkedChipIds
        val size = ids.size

        when {
            //none selected
            size < 1 -> {
                findViewById<TextView>(R.id.warning_p7_1).visibility = View.VISIBLE
            }
            //selected more than allowed
            size > SELECTION_LIMIT -> {
                findViewById<TextView>(R.id.warning_p7_2).visibility = View.VISIBLE
            }
            //correct numbers of selection
            else -> {
                getCheckedChip()
                val intent = Intent(this, ProfileAudioRecording::class.java)
                setUser()
                startActivity(intent)
            }
        }
    }

    /**
     * Iterates through the checked chips and gets the passions
     */
    private fun getCheckedChip() {
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroup_p7)
        val ids = chipGroup.checkedChipIds
        ids.forEach { id ->
            val chipText = findViewById<Chip>(id).text.toString()
            passions.add(chipText)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUser() {
        val location = getCurrentAddress()
        userBuilder.setLocation(location)
            .setPassions(passions)
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