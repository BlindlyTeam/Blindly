package ch.epfl.sdp.blindly

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AudioLibrary : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_library)

        supportActionBar?.hide()
    }
}