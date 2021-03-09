package ch.epfl.sdp.blindly.recording

import android.media.MediaRecorder
import android.media.MediaRecorder.AudioSource.MIC
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ch.epfl.sdp.blindly.R
import java.io.File
import java.io.IOException

class RecordingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)
        createRecorder()
    }

    private fun createRecorder(): MediaRecorder {
        val recorder = MediaRecorder()
        recorder.setAudioSource(MIC)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)

        val filePath = applicationContext.filesDir.path
        val audioFile = File(filePath)

        if(!audioFile.exists()) {
            audioFile.mkdirs()
        }

        recorder.setOutputFile("$audioFile.3gp")
        return recorder;
    }

    fun prepareRecording(recorder: MediaRecorder) {
        try {
            recorder.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "File creation failed : ${e.message}",
                    Toast.LENGTH_SHORT).show()
        }

    }

    fun startRecording(recorder: MediaRecorder) {
        recorder.start()
    }

    fun stopRecording(recorder: MediaRecorder) {
        recorder.stop()
    }

}