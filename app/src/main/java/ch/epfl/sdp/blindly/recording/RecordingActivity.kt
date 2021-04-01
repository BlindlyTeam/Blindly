package ch.epfl.sdp.blindly.recording

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.*
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.profile.ProfileFinished
import java.io.File
import java.io.IOException

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class RecordingActivity : AppCompatActivity(), AudioLibraryAdapter.OnItemClickListener {
    private val mediaRecorder = MediaRecorder()

    private lateinit var recordingRecyclerView: RecyclerView
    private lateinit var adapter: AudioLibraryAdapter

    private var isRecording = false

    private var recordFilePath = ""

    private lateinit var recordButton: Button
    private lateinit var recordTimer: Chronometer

    private var totalNumberOfRec = 0

    var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recording)

        setBaseView()
        changeRecordFilePath(totalNumberOfRec)
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        recordTimer = findViewById(R.id.recordTimer)

        // For the recording list
        recordingRecyclerView = findViewById(R.id.recordingList)
        recordingRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AudioLibraryAdapter(ArrayList(), this, this)
        recordingRecyclerView.adapter = adapter
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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

        //deleteTempRecordings()
        // TODO Delete Temp recordings when link with AudioLibrary
        adapter.recordList = ArrayList()
    }

    fun startProfileFinished(v: View) {
        val intent = Intent(this, ProfileFinished::class.java)
        startActivity(intent)
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

    private fun setBaseView() {
        recordButton = findViewById(R.id.recordingButton)
        recordTimer = findViewById(R.id.recordTimer)
        bindRecordButton(recordButton)
        // TODO: Make the timer show milliseconds
    }

    private fun setRecordView() {
        recordTimer.base = SystemClock.elapsedRealtime()
        recordTimer.start()
        isRecording = true
    }

    private fun setFinishedRecordView() {
        recordTimer.stop()
        isRecording = false
    }

    private fun prepareRecording() {
        changeRecordFilePath(totalNumberOfRec)
        mediaRecorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB)
            setOutputFile(recordFilePath)
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
        adapter.mediaPlayer?.stop()
        prepareRecording()
        mediaRecorder.start()
    }

    private fun stopRecording() {
        mediaRecorder.stop()

        val newAudio = AudioRecord("Audio file ${totalNumberOfRec + 1}",
            recordTimer.text as String, recordFilePath, false)
        adapter.recordList.add(newAudio)
        adapter.notifyDataSetChanged()

        totalNumberOfRec++
        setFinishedRecordView()
    }

    private fun changeRecordFilePath(recordNumber: Int) {
        // Recordings are stored in the internal storage of the app, only accessible by the app itself
        recordFilePath = "${applicationContext.filesDir.absolutePath}/TEMPaudioRecording_${recordNumber}.3gp"
    }

    private fun deleteTempRecordings() {
        for(i in 0..totalNumberOfRec) {
            changeRecordFilePath(i)
            File(recordFilePath).delete()
        }
    }

    private fun bindRecordButton(button: Button) {
        val bounce = AnimationUtils.loadAnimation(this, R.anim.bouncy_button)
        button.setOnClickListener {
            button.startAnimation(bounce)
            recordButtonClick(it)
        }
    }

    override fun onItemClick(position: Int) {
        adapter.notifyItemChanged(position)
    }
}