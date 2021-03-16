package ch.epfl.sdp.blindly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SettingsShowMe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_show_me)

        supportActionBar?.hide()
    }
}