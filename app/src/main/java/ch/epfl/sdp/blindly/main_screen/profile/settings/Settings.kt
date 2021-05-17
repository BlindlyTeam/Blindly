package ch.epfl.sdp.blindly.main_screen.profile.settings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.SplashScreen
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.user.AGE_RANGE
import ch.epfl.sdp.blindly.user.RADIUS
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.UserHelper.Companion.DEFAULT_RADIUS
import ch.epfl.sdp.blindly.viewmodel.UserViewModel
import ch.epfl.sdp.blindly.viewmodel.ViewModelAssistedFactory
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val EXTRA_SHOW_ME = "showMe"
const val EXTRA_LOCATION = "location"
private const val MIN_AGE = 18
private const val MAX_AGE = 99

/**
 * Activity class for the settings of the app and the user
 */
@AndroidEntryPoint
class Settings : AppCompatActivity() {

    @Inject
    lateinit var userHelper: UserHelper

    @Inject
    lateinit var assistedFactory: ViewModelAssistedFactory

    private lateinit var viewModel: UserViewModel

    private var currentRadius: Int = DEFAULT_RADIUS
    private lateinit var currentAgeRange: List<Int>
    private lateinit var radiusSlider: Slider
    private lateinit var ageRangeSlider: RangeSlider

    companion object {
        const val TAG = "Settings"
    }

    /**
     * Creates the activity window
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val uid = userHelper.getUserId()
        viewModel = UserViewModel.instantiateViewModel(
            uid,
            assistedFactory,
            this,
            this
        )

        val emailAddressText = findViewById<TextView>(R.id.email_address_text)
        emailAddressText.text = userHelper.getEmail() ?: getString(R.string.not_logged_in)

        val location = findViewById<TextView>(R.id.current_location_text)

        val radius = findViewById<TextView>(R.id.radius_text)
        radiusSlider = findViewById(R.id.location_slider)

        val ageRange = findViewById<TextView>(R.id.selected_age_range_text)
        ageRangeSlider = findViewById(R.id.age_range_slider)

        val showMe = findViewById<TextView>(R.id.show_me_text)

        //Retrieve settings from database
        viewModel.user.observe(this) {
            location.text = AndroidLocationService.getCurrentLocationStringFromUser(this, it)
            this.currentRadius = it.radius ?: DEFAULT_RADIUS
            currentAgeRange = it.ageRange ?: listOf(MIN_AGE, MAX_AGE)
            radiusSlider.value = this.currentRadius.toFloat()
            showMe.text = it.showMe
            ageRangeSlider.setValues(
                currentAgeRange[0].toFloat(),
                currentAgeRange[1].toFloat()
            )
        }

        //Update the currentRadius text with initial value, and everytime the slider changes
        radius.text = getString(R.string.progress_km, radiusSlider.value.toInt())
        radiusSlider.addOnChangeListener { _, value, _ ->
            radius.text = getString(R.string.progress_km, value.toInt())
        }

        //Update the selected age range text with initial value, and everytime the slider changes
        ageRange.text = getAgeRangeString(ageRangeSlider)
        ageRangeSlider.addOnChangeListener { _, _, _ ->
            ageRange.text = getAgeRangeString(ageRangeSlider)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onResume() {
        super.onResume()
        viewModel.userUpdate()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBackPressed() {
        if (radiusSlider.value != currentRadius.toFloat()) {
            viewModel.updateField(RADIUS, radiusSlider.value.toInt())
        }
        if (ageRangeSlider.values != listOf(
                currentAgeRange[0].toFloat(),
                currentAgeRange[1].toFloat()
            )
        ) {
            viewModel.updateField(
                AGE_RANGE,
                listOf(ageRangeSlider.values[0].toInt(), ageRangeSlider.values[1].toInt())
            )
        }
        super.onBackPressed()
    }

    private fun getAgeRangeString(slider: RangeSlider): String {
        return getString(
            R.string.selected_age_range,
            slider.values[0].toInt(),
            slider.values[1].toInt()
        )
    }

    /**
     * Called by the Location button
     *
     * @param view
     */
    fun startLocationSettings(view: View) {
        val location = findViewById<TextView>(R.id.current_location_text).text.toString()
        val intent = Intent(this, SettingsLocation::class.java).apply {
            putExtra(EXTRA_LOCATION, location)
        }
        startActivity(intent)
    }

    /**
     * Called by the ShowMe button
     *
     * @param view
     */
    fun startShowMeSettings(view: View) {
        val currentShowMe = findViewById<TextView>(R.id.show_me_text).text.toString()
        val intent = Intent(this, SettingsShowMe::class.java).apply {
            putExtra(EXTRA_SHOW_ME, currentShowMe)
        }
        startActivity(intent)
    }

    /**
     * Called by the Logout button
     *
     * @param view
     */
    fun logout(view: View) {
        userHelper.logout(this)
            .addOnCompleteListener { // user is now signed out
                startActivity(Intent(this, SplashScreen::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.logout_error),
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    /**
     * Called by the Delete account button
     *
     * @param view
     */
    fun deleteAccount(view: View) {
        //For now, just return to the main activity
        startActivity(Intent(this, SplashScreen::class.java))
    }

    /**
     * Called by the UpdateEmail button
     *
     * @param view
     */
    fun startUpdateEmail(view: View) {
        startActivity(Intent(this, SettingsUpdateEmail::class.java))
    }
}