package ch.epfl.sdp.blindly

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException

class AudioLibrary : AppCompatActivity(), RecordingAdapter.OnItemClickListener {
    private lateinit var recordingList: RecyclerView
    private lateinit var adapter: RecordingAdapter

    private lateinit var recordingsSet: Array<MediaPlayer?>
    private lateinit var recordingsNames: Array<String>

    private var isPlayingStopped = true
    private var currentlyPlayedRecording = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_library)

        supportActionBar?.hide()

        recordingsNames = applicationContext.filesDir.list()
        recordingsNames.sort() // Get recording names in alphabetical order
        recordingsSet = loadRecordings(recordingsNames)

        recordingList = findViewById(R.id.recordingList)
        recordingList.layoutManager = LinearLayoutManager(this)
        adapter = RecordingAdapter(recordingsNames, recordingsSet, this)
        recordingList.adapter = adapter
    }

    override fun onItemClick(position: Int) {
        //Toast.makeText(this, "You clicked item $position", Toast.LENGTH_SHORT).show()
        //recordingsNames[position] += " | playing";
        if(isPlayingStopped || position == currentlyPlayedRecording) {
            playPauseRecording(recordingsSet[position], position)
        }
        adapter.notifyItemChanged(position)
    }

    private fun playPauseRecording(recording: MediaPlayer?, position: Int){
        // Preparation
        if(isPlayingStopped){
            preparePlaying(recording)
            currentlyPlayedRecording = position
        }

        if(!recording!!.isPlaying){
            isPlayingStopped = false
            recording.start()
        } else{
            recording.pause()
        }
    }

    private fun preparePlaying(recording: MediaPlayer?) {
        try {
            recording?.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "MediaPlayer preparation failed : ${e.message}",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadRecordings(recordingsNames: Array<String>): Array<MediaPlayer?> {
        var recordingsSet: Array<MediaPlayer?> = arrayOfNulls<MediaPlayer>(recordingsNames.size)
        for((count, name) in recordingsNames.withIndex()){
            var recording: MediaPlayer = MediaPlayer().apply {
                setDataSource("${applicationContext.filesDir.absolutePath}/$name")
            }
            recording.setOnCompletionListener {
                recording.stop()
                isPlayingStopped = true
                currentlyPlayedRecording = -1
            }
            recordingsSet[count] = recording
        }
        return recordingsSet
    }
}