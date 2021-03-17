package ch.epfl.sdp.blindly.recording

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import ch.epfl.sdp.blindly.R
import java.io.IOException
import java.util.*

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class RecordingActivity : AppCompatActivity() {
    private val mediaRecorder = MediaRecorder()
    private var mediaPlayer: MediaPlayer? = null

    private var fileName: String = ""

    private var isRecording = false
    private var isPlaying = false

    private lateinit var recordButton: Button
    private lateinit var playPauseButton: Button
    private lateinit var recordText: TextView
    private lateinit var playBar: SeekBar

    var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)

        recordButton = findViewById(R.id.recordingButton)
        playPauseButton = findViewById(R.id.playingButton)
        playPauseButton.isEnabled = false

        recordText = findViewById(R.id.recordingText)
        recordText.isVisible = false

        playBar = findViewById(R.id.playBar)
        playBar.isVisible = false

        // Record to the external cache for now
        fileName = "${externalCacheDir?.absolutePath}/audioRecordTest.3gp"

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) finish()
    }

    override fun onStop() {
        super.onStop()
        mediaRecorder.release()
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

    fun playPauseButtonClick(view: View) {
        if (!isPlaying) {
            startPlaying()
            playPauseButton.text = "Pause"
        } else {
            pausePlaying()
            playPauseButton.text = "Play"
        }
    }

    private fun prepareRecording() {
        mediaRecorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB)
            setOutputFile(fileName)
        }
        try {
            mediaRecorder.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "File creation failed : ${e.message}",
                    Toast.LENGTH_SHORT).show()
        }
    }

    private fun startRecording() {
        prepareRecording()
        isRecording = true
        mediaRecorder.start()
        recordText.isVisible = true
        recordText.text = "Recording..."
        playPauseButton.isEnabled = false
    }

    private fun stopRecording() {
        isRecording = false
        mediaRecorder.stop()
        playPauseButton.isEnabled = true
        recordText.text = "Done !"
        playBar.isVisible = true

        if (mediaPlayer == null) mediaPlayer = createPlayer()
        preparePlaying()
        playPauseButton.isEnabled = true
    }

    private fun createPlayer(): MediaPlayer {
        return MediaPlayer().apply {
            setDataSource(fileName)
        }
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
        isPlaying = true
        recordButton.isEnabled = false
        mediaPlayer?.start()
        updatePlayBar(mediaPlayer!!.duration, mediaPlayer!!.currentPosition)
    }

    private fun pausePlaying() {
        isPlaying = false
        recordButton.isEnabled = true
        mediaPlayer?.pause()
    }

    private fun updatePlayBar(duration: Int, position: Int) {
        playBar.max = duration
        playBar.progress = position

        val handler = Handler(Looper.getMainLooper())
        handler.removeCallbacks(movePlayBarThread);
        handler.postDelayed(movePlayBarThread, 100);
    }

    private val movePlayBarThread: Runnable = object : Runnable {
        override fun run() {
            if (mediaPlayer?.isPlaying == true) {
                val newMediaPos = mediaPlayer!!.currentPosition
                val newMediaMax = mediaPlayer!!.duration
                playBar.max = newMediaMax
                playBar.progress = newMediaPos
                Handler(Looper.getMainLooper()).postDelayed(this, 100)
            }
        }
    }
}