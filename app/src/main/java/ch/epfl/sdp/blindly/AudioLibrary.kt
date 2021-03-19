package ch.epfl.sdp.blindly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AudioLibrary : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_library)

        supportActionBar?.hide()
    }
}