package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.recording.RecordingActivity

/**
 * Page to prompt user to record an audio file for their profile.
 */
class ProfileAudioRecording : AppCompatActivity() {
    private lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_audio_recording)

        bundle = intent.extras!!
    }

    fun startRecordingActivity(view: View) {
        val intent = Intent(this, RecordingActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}