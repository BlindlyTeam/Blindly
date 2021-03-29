package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.recording.RecordingActivity

class ProfileAudioRecording : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_audio_recording)
    }

    fun startRecordingActivity(view: View) {
        val intent = Intent(this, RecordingActivity::class.java)
        startActivity(intent)
    }
}