package ch.epfl.sdp.blindly.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.MainActivity
import ch.epfl.sdp.blindly.R
import com.google.android.gms.tasks.OnCompleteListener
import ch.epfl.sdp.blindly.profile_setup.EXTRA_SHOW_ME
import ch.epfl.sdp.blindly.utils.UserHelper
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val EXTRA_LOCATION = "user_location"
const val EXTRA_SHOW_ME = "show_me"

const val REQUEST_SHOW_ME = 2

/**
 * Activity class for the settings of the app and the user
 *
 */
@AndroidEntryPoint
class Settings : AppCompatActivity() {

    @Inject
    lateinit var user: UserHelper

    /**
     * Creates the activity window
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.hide()

        val emailAddressText = findViewById<TextView>(R.id.email_address_text)
        emailAddressText.text = user.getEmail() ?: getString(R.string.not_logged_in)

        val locationText = findViewById<TextView>(R.id.current_location_text)
        locationText.text = getString(R.string.lausanne_switzerland)

        val radiusText = findViewById<TextView>(R.id.radius_text)
        val radiusSlider = findViewById<Slider>(R.id.location_slider)

        //Update the radius text with initial value, and everytime the slider changes
        radiusText.text = getString(R.string.progress_km, radiusSlider.value.toInt())
        radiusSlider.addOnChangeListener { _, value, _ ->
            radiusText.text = getString(R.string.progress_km, value.toInt())
        }

        val ageRangeText = findViewById<TextView>(R.id.selected_age_range_text)
        val ageRangeSlider = findViewById<RangeSlider>(R.id.age_range_slider)

        //Update the selected age range text with initial value, and everytime the slider changes
        ageRangeText.text = getAgeRangeString(ageRangeSlider)
        ageRangeSlider.addOnChangeListener { _, _, _ ->
            ageRangeText.text = getAgeRangeString(ageRangeSlider)
        }

        val showMe = findViewById<TextView>(R.id.show_me_text)
        showMe.text = getString(R.string.women_show_me)
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
        val currentLocation = findViewById<TextView>(R.id.current_location_text).text.toString()
        val intent = Intent(this, SettingsLocation::class.java).apply {
            putExtra(EXTRA_LOCATION, currentLocation)
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
        //TODO fix me
        startActivityForResult(intent, REQUEST_SHOW_ME)
    }

    /**
     * Called by the Logout button
     *
     * @param view
     */
    fun logout(view: View) {
        /*user.signOut(this, OnCompleteListener {
            OnCompleteListener<Void> { p0 ->
                if (p0.isComplete)
                    startActivity(Intent(this@Settings, MainActivity::class.java))
                else
                // TODO fixme
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.logout_error),
                        Toast.LENGTH_LONG
                    ).show()
            }
        })*/ //TODO the logout doesn't work for now
        startActivity(Intent(this, MainActivity::class.java))
    }

    /**
     * Called by the Delete account button
     *
     * @param view
     */
    fun deleteAccount(view: View) {
        //For now, just return to the main activity
        startActivity(Intent(this, MainActivity::class.java))
    }

    // This is the non depracated version but it crashes for the moment
    /*private fun startActivityForResult(extra: String): ActivityResultLauncher<Intent> {
        return registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    if (data.hasExtra(EXTRA_SHOW_ME) && extra == EXTRA_SHOW_ME) {
                        val showMe = findViewById<TextView>(R.id.show_me_text)
                        showMe.text = data.getStringExtra(EXTRA_SHOW_ME)
                    }
                }
            }
        }
    }*/

    /**
     * TODO
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == RESULT_OK && requestCode == REQUEST_SHOW_ME) {
            if (intent != null) {
                if (intent.hasExtra(EXTRA_SHOW_ME)) {
                    val showMe = findViewById<TextView>(R.id.show_me_text)
                    showMe.text = intent.getStringExtra(EXTRA_SHOW_ME)
                }
            }
        }
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