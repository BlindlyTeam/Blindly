package ch.epfl.sdp.blindly.profile_setup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.recording.RecordingActivity
import ch.epfl.sdp.blindly.user.User
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ProfileAudioRecording : AppCompatActivity() {

    private lateinit var userBuilder: User.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setup_audio_recording)

        val bundle = intent.extras
        if (bundle != null) {
            userBuilder = bundle.getString(EXTRA_USER)?.let { Json.decodeFromString(it) }!!
        }
    }

    fun startRecordingActivity(view: View) {
        val bundle = Bundle()
        bundle.putSerializable(
            EXTRA_USER,
            Json.encodeToString(User.Builder.serializer(), userBuilder)
        )
        val intent = Intent(this, RecordingActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}