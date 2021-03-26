package ch.epfl.sdp.blindly.recording

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R
import java.io.File
import java.io.IOException

class AudioLibrary : AppCompatActivity(), RecordingAdapter.OnItemClickListener {
    private lateinit var recordingList: RecyclerView
    private lateinit var adapter: RecordingAdapter

    private lateinit var recordings: ArrayList<AudioRecord>

    private var mediaPlayer: MediaPlayer? = null

    private var currentlyPlayedRecording = -1
    private var lastSelectionPos = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_library)

        supportActionBar?.hide()

        //recordings = applicationContext.filesDir.list()
        //recordings.sort() // Get recording names in alphabetical order

        recordingList = findViewById(R.id.recordingList)
        recordingList.layoutManager = LinearLayoutManager(this)
        adapter = RecordingAdapter(recordings.toCollection(ArrayList()), this, this)
        recordingList.adapter = adapter
    }

    override fun onItemClick(position: Int) {
        selectDeselectRecording(position)
        playPauseRecording(position)

        adapter.notifyItemChanged(position)
    }

    private fun playPauseRecording(position: Int){
        // We select another recording when another one is already playing
        if(currentlyPlayedRecording != position && currentlyPlayedRecording != -1){
            // Stop the current recording
            mediaPlayer?.stop()
            currentlyPlayedRecording = -1
        }
        // No recording is playing
        if(currentlyPlayedRecording == -1){
            mediaPlayer = initNewMediaPlayer(recordings[position].name)
            preparePlaying(mediaPlayer)
            currentlyPlayedRecording = position
        }

        if(!mediaPlayer!!.isPlaying){
            mediaPlayer?.start()
        } else{
            mediaPlayer?.pause()
        }
    }

    private fun selectDeselectRecording(position: Int){
        // Logic behind (De)selection of recordings
        adapter.currentSelectionPos = position
        lastSelectionPos = if(lastSelectionPos == -1) {
            adapter.currentSelectionPos
        } else {
            adapter.notifyItemChanged(lastSelectionPos)
            adapter.currentSelectionPos
        }
        adapter.notifyItemChanged(adapter.currentSelectionPos)
    }

    private fun initNewMediaPlayer(name: String): MediaPlayer?{
        val mediaPlayer: MediaPlayer? = MediaPlayer().apply {
            setDataSource("${applicationContext.filesDir.absolutePath}/$name")
        }
        mediaPlayer?.setOnCompletionListener {
            mediaPlayer?.stop()
            currentlyPlayedRecording = -1
        }
        return mediaPlayer
    }

    private fun preparePlaying(mediaPlayer: MediaPlayer?) {
        try {
            mediaPlayer?.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "MediaPlayer preparation failed : ${e.message}",
                Toast.LENGTH_SHORT).show()
        }
    }

    fun sendMessage(view: View){
        saveCurrentRecording()
    }

    private fun saveCurrentRecording(){
        if(adapter.currentSelectionPos != -1) {
            val tempName = recordings[currentlyPlayedRecording].name
            val newName = "PRINCIPALaudioRecording.3gp"
            val currentRecording = File("${applicationContext.filesDir.absolutePath}/$tempName")
            currentRecording.copyTo(File("${applicationContext.filesDir.absolutePath}/$newName"), overwrite = true)
        } else {
            Toast.makeText(this, "Select a recording !", Toast.LENGTH_SHORT).show()
        }
    }
}