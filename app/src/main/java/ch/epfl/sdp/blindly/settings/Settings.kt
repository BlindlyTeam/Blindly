package ch.epfl.sdp.blindly.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.hide()
        val context = this@Settings
        val emailAddressText = findViewById<TextView>(R.id.email_address_text)
        emailAddressText.text = "random@epfl.ch"

        val locationText = findViewById<TextView>(R.id.current_location_text)
        locationText.text = "Lausanne, Switzerland"

        val radiusText = findViewById<TextView>(R.id.radius_text)
        val radiusSeekBar = findViewById<SeekBar>(R.id.seekBar)

        radiusSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                radiusText.text = progress.toString() + "km"
            }

            override fun onStartTrackingTouch(seek: SeekBar) {}
            override fun onStopTrackingTouch(seek: SeekBar) {}
        })

        val showMe = findViewById<TextView>(R.id.show_me_text)
        showMe.text = "Women"

    }

    fun startLocationSettings(view: View) {
        val intent = Intent(this, SettingsLocation::class.java)
        startActivity(intent)
    }

    fun startShowMeSettings(view: View) {
        val intent = Intent(this, SettingsShowMe::class.java)
        startActivity(intent)
    }

}