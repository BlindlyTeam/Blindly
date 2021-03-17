package ch.epfl.sdp.blindly.settings

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R

class SettingsLocation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_location)

        supportActionBar?.hide()

        val currentLocation = intent.getStringExtra(EXTRA_LOCATION)
        findViewById<TextView>(R.id.my_current).apply {
            text = currentLocation
        }
    }
}