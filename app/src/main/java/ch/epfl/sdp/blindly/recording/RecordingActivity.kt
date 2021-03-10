package ch.epfl.sdp.blindly.recording

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import ch.epfl.sdp.blindly.R
import java.io.IOException

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class RecordingActivity : AppCompatActivity() {
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null

    private var fileName: String = ""

    private var isRecording = false
    private var isPlaying = false

    private lateinit var recordButton: Button
    private lateinit var playButton: Button
    private lateinit var recordText: TextView

    var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)

        recordButton = findViewById(R.id.recordingButton)
        playButton = findViewById(R.id.playingButton)
        playButton.isEnabled = false
        recordText = findViewById(R.id.recordingText)
        recordText.isVisible = false

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        // Record to the external cache for now
        fileName = "${externalCacheDir?.absolutePath}/audioRecordTest.3gp"
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) finish()
    }

    override fun onStop() {
        super.onStop()
        mediaRecorder?.release()
        mediaRecorder = null
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun recordButtonClick(view: View) {
        if (!isRecording) {
            startRecording()
            recordButton.text = "Stop recording"
        } else {
            stopRecording()
            recordButton.text = "Start recording"
        }
    }

    fun playButtonClick(view: View) {
        if (!isPlaying) {
            startPlaying()
            playButton.text = "Stop playing"
        } else {
            stopPlaying()
            playButton.text = "Start playing"
        }
    }

    private fun createRecorder(): MediaRecorder {
        val recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB)
            setOutputFile(fileName)
        }
        return recorder
    }

    private fun prepareRecording() {
        try {
            mediaRecorder?.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "File creation failed : ${e.message}",
                    Toast.LENGTH_SHORT).show()
        }
    }

    private fun startRecording() {
        mediaRecorder = createRecorder()
        isRecording = true
        prepareRecording()
        mediaRecorder?.start()
        recordText.isVisible = true
    }

    private fun stopRecording() {
        isRecording = false
        mediaRecorder?.stop()
        mediaRecorder?.release()
        playButton.isEnabled = true
        recordText.text = "Done !"
    }

    private fun createPlayer(): MediaPlayer {
        val player = MediaPlayer()
        player.setDataSource(fileName)
        return player
    }

    private fun preparePlaying() {
        try {
            mediaPlayer?.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "MediaPlayer preparation failed : ${e.message}",
                    Toast.LENGTH_SHORT).show()
        }
    }

    private fun startPlaying() {
        mediaPlayer = createPlayer()
        isPlaying = true
        preparePlaying()
        mediaPlayer?.start()
    }

    private fun stopPlaying() {
        mediaPlayer?.release()
    }

}