package ch.epfl.sdp.blindly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ch.epfl.sdp.blindly.recording.RecordingActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun launchRecording(view: View) {
        val intent = Intent(this, RecordingActivity::class.java)
        startActivity(intent)
    }
}