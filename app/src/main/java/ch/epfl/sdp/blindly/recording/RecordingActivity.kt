package ch.epfl.sdp.blindly.recording

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R
import java.io.File
import java.io.IOException


private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
private const val MAXIMUM_AUDIO_DURATION = 90000

/**
 * Activity that contains everything to record audio files and listen to them to select the one
 * we want to keep.
 */
class RecordingActivity : AppCompatActivity(), AudioLibraryAdapter.OnItemClickListener {
    private val mediaRecorder = MediaRecorder()

    private lateinit var recordingRecyclerView: RecyclerView
    private lateinit var adapter: AudioLibraryAdapter

    private var isRecording = false

    private var recordFilePath = ""

    private lateinit var recordButton: Button
    private lateinit var recordTimer: Chronometer
    private lateinit var remainingRecordTimer: Chronometer

    private var totalNumberOfRec = 0

    var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    /**
     * Binds the audio record list to the adapter, sets the base view and initialise values
     * declared in the class.
     *
     * @param savedInstanceState
     */
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)

        setBaseView()
        changeRecordFilePath(totalNumberOfRec)
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        // Initialise the RecyclerView that will contain the recordings.
        recordingRecyclerView = findViewById(R.id.recordingList)
        recordingRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AudioLibraryAdapter(ArrayList(), ArrayList(), this, this)
        recordingRecyclerView.adapter = adapter
    }

    /**
     * Requests permissions to record, using the device's microphone.
     *
     * @param requestCode the code of the requested permission
     * @param permissions the array of the accepted permissions
     * @param grantResults the array of permission request results represented as integers
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
        deleteTempRecordings()
        adapter.recordList = ArrayList()
    }

    override fun onItemClick(position: Int) {
        adapter.notifyItemChanged(position)
    }

    private fun recordButtonClick(view: View) {
        if (!isRecording) {
            startRecording()
            setRecordView()
        } else {
            stopRecording()
            setFinishedRecordView()
        }
    }

    /**
     * Adds a bounce animation and the start/stop record ability to a button
     *
     * @param button the button to be binded
     */
    private fun bindRecordButton(button: Button) {
        val bounce = AnimationUtils.loadAnimation(this, R.anim.bouncy_button)
        button.setOnClickListener {
            button.startAnimation(bounce)
            recordButtonClick(it)
        }
    }

    // Setting a chronometer to count down requires Android N
    @RequiresApi(Build.VERSION_CODES.N)
    private fun setBaseView() {
        recordButton = findViewById(R.id.recordingButton)
        recordTimer = findViewById(R.id.recordTimer)
        remainingRecordTimer = findViewById(R.id.remainingRecordTimer)
        remainingRecordTimer.base = SystemClock.elapsedRealtime() + MAXIMUM_AUDIO_DURATION.toLong()
        remainingRecordTimer.isCountDown = true

        // When 10 seconds remain, the text becomes red
        remainingRecordTimer.setOnChronometerTickListener {
            if (it.text == "Remaining:\n00:10") {
                it.setTextColor(resources.getColor(R.color.bright_red, theme))
            }
        }
        bindRecordButton(recordButton)
    }

    private fun setRecordView() {
        adapter.collapseLayouts()

        // Reset both timers
        recordTimer.base = SystemClock.elapsedRealtime()
        remainingRecordTimer.base = SystemClock.elapsedRealtime() + MAXIMUM_AUDIO_DURATION.toLong()

        remainingRecordTimer.setTextColor(Color.BLACK)
        recordTimer.start()
        remainingRecordTimer.start()
        isRecording = true
    }

    private fun setFinishedRecordView() {
        recordTimer.stop()
        remainingRecordTimer.stop()
        isRecording = false
    }

    /**
     * Builds a new file path for the next recording, sets everything for the media recorder and
     * prepare it.
     */
    private fun prepareRecording() {
        changeRecordFilePath(totalNumberOfRec)
        mediaRecorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB)
            setOutputFile(recordFilePath)
            setMaxDuration(MAXIMUM_AUDIO_DURATION)
            // If the record reaches the max duration, it stops and the view is set accordingly
            setOnInfoListener { _, what, _ ->
                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    stopRecording()
                    setFinishedRecordView()
                }
            }
        }
        try {
            mediaRecorder.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(
                this, "File creation failed : ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun startRecording() {
        adapter.mediaPlayer?.stop()
        prepareRecording()
        mediaRecorder.start()
    }

    /**
     * Stops the recording and store the audio file in the app's local cache.
     */
    private fun stopRecording() {
        mediaRecorder.stop()

        val newAudio = AudioRecord(
            "Audio file ${totalNumberOfRec + 1}",
            recordTimer.text as String, recordFilePath, false
        )
        adapter.recordList.add(newAudio)
        adapter.notifyDataSetChanged()

        totalNumberOfRec++
        setFinishedRecordView()
    }

    /**
     * Create a new file path for the audio file. The files are store in the internal storage of
     * the app, only accessible by the app itself.
     *
     * @param recordNumber the number of the file we will create
     */
    private fun changeRecordFilePath(recordNumber: Int) {
        recordFilePath =
            "${applicationContext.filesDir.absolutePath}/TEMPaudioRecording_${recordNumber}.3gp"
    }

    private fun deleteTempRecordings() {
        for (i in 0..totalNumberOfRec) {
            changeRecordFilePath(i)
            File(recordFilePath).delete()
        }
    }
}