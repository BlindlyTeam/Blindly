package ch.epfl.sdp.blindly.recording

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.*
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import ch.epfl.sdp.blindly.R
import java.io.IOException

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class RecordingActivity : AppCompatActivity() {
    private val mediaRecorder = MediaRecorder()
    private var mediaPlayer: MediaPlayer? = null

    private var isPlayerStopped = true

    private var fileName: String = ""

    private var isRecording = false

    private lateinit var recordButton: Button
    private lateinit var playPauseButton: Button
    private lateinit var playBar: SeekBar
    private lateinit var recordText: TextView
    private lateinit var recordTimer: Chronometer
    private lateinit var playTimer: Chronometer

    var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)
        setBaseView()

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
            setRecordView()
        } else {
            stopRecording()
            setFinishedRecordView()
        }
    }

    fun playPauseButtonClick(view: View) {
        if (isPlayerStopped) preparePlaying()
        if (!mediaPlayer!!.isPlaying) {
            mediaPlayer?.start()
            setPlayView()
        } else {
            mediaPlayer?.pause()
            setPauseView()
        }
    }

    private fun setBaseView() {
        recordButton = findViewById(R.id.recordingButton)
        playPauseButton = findViewById(R.id.playingButton)
        playPauseButton.isEnabled = false

        playBar = findViewById(R.id.playBar)
        playBar.isVisible = false

        recordText = findViewById(R.id.recordingText)
        recordText.isVisible = false

        recordTimer = findViewById(R.id.recordTimer)
        playTimer = findViewById(R.id.playTimer)
    }

    private fun setPlayView() {
        isPlayerStopped = false
        playTimer.base = SystemClock.elapsedRealtime()
        playTimer.start()
        playPauseButton.text = "Pause"
        recordButton.isEnabled = false
        updatePlayBar(mediaPlayer!!.duration, mediaPlayer!!.currentPosition)
    }

    private fun setPauseView() {
        recordButton.isEnabled = true
        playTimer.stop()
        playPauseButton.text = "Play"
    }

    private fun setFinishedPlayView() {
        playPauseButton.text = "Play"
        recordButton.isEnabled = true
        playTimer.stop()
        playBar.progress = 0
        isPlayerStopped = true
    }

    private fun setRecordView() {
        isRecording = true
        recordButton.isVisible = true
        recordText.isVisible = true
        recordText.text = "Recording..."
        playPauseButton.isEnabled = false
        playBar.progress = 0
        recordTimer.base = SystemClock.elapsedRealtime()
        recordTimer.start()
        recordButton.text = "Stop recording"
    }

    private fun setFinishedRecordView() {
        isRecording = false
        playPauseButton.isEnabled = true
        recordText.text = "Done !"
        playBar.isVisible = true
        recordButton.text = "Start recording"
        recordTimer.stop()
        playPauseButton.isEnabled = true
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
        mediaPlayer?.stop()
        prepareRecording()
        mediaRecorder.start()
    }

    private fun stopRecording() {
        mediaRecorder.stop()
        setFinishedRecordView()

        if (mediaPlayer == null) mediaPlayer = createPlayer()
    }

    private fun createPlayer(): MediaPlayer {
        val player = MediaPlayer().apply {
            setDataSource(fileName)
        }
        player.setOnCompletionListener {
            mediaPlayer?.stop()
            setFinishedPlayView()
        }
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