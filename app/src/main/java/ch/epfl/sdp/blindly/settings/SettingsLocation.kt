package ch.epfl.sdp.blindly.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.epfl.sdp.blindly.R

class SettingsLocation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_location)

        supportActionBar?.hide()
    }
}