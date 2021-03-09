package ch.epfl.sdp.blindly.recording

import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import ch.epfl.sdp.blindly.R
import java.io.File
import java.io.IOException

class RecordingActivity : AppCompatActivity() {
    private lateinit var mediaRecorder: MediaRecorder

    private var fileName: String = ""

    private var isRecording = false
    private var isListening = false

    private lateinit var recordButton: Button
    private lateinit var listenButton: Button
    private lateinit var recordText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)

        recordButton = findViewById(R.id.recordingButton)
        listenButton = findViewById(R.id.listeningButton)
        recordText = findViewById(R.id.validationText)

        mediaRecorder = createRecorder()

        // Record to the external cache for now
        fileName = "${externalCacheDir?.absolutePath}/audioRecordTest.3gp"
    }

    private fun createRecorder(): MediaRecorder {
        val recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(fileName)
        }

        return recorder
    }

    fun prepareRecording() {
        try {
            mediaRecorder.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "File creation failed : ${e.message}",
                    Toast.LENGTH_SHORT).show()
        }

    }

    fun recordButtonClick() {
        if (!isRecording) {
            startRecording()
            recordButton.text = "Stop recording"
        } else {
            stopRecording()
            recordButton.text = "Start recording"
        }
    }

    fun listenButtonClick() {
        if (!isListening) {
            startListening()
            listenButton.text = "Stop listening"
        } else {
            stopRecording()
            listenButton.text = "Start listening"
        }
    }

    fun startListening() {

    }

    fun stopListening() {

    }

    fun startRecording() {
        isRecording = true
        prepareRecording()
        mediaRecorder.start()
    }

    fun stopRecording() {
        isRecording = false
        mediaRecorder.stop()
        mediaRecorder.reset()
    }

}