package ch.epfl.sdp.blindly.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.recording.RecordingActivity

class ProfileAudioRecording : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_profile_8)
    }

    fun startRecordingActivity(view: View) {
        val intent = Intent(this, RecordingActivity::class.java)
        startActivity(intent)
    }
}