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

    private lateinit var recordingsNames: Array<String>

    private var mediaPlayer: MediaPlayer? = null

    private var isPlayingStopped = true
    private var currentlyPlayedRecording = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_library)

        supportActionBar?.hide()

        recordingsNames = applicationContext.filesDir.list()
        recordingsNames.sort() // Get recording names in alphabetical order

        recordingList = findViewById(R.id.recordingList)
        recordingList.layoutManager = LinearLayoutManager(this)
        adapter = RecordingAdapter(recordingsNames.toCollection(ArrayList()), this)
        recordingList.adapter = adapter
    }

    override fun onItemClick(position: Int) {
        if(isPlayingStopped || position == currentlyPlayedRecording) {
            playPauseRecording(position)
        }
        adapter.notifyItemChanged(position)
    }

    private fun playPauseRecording(position: Int){
        // Preparation
        if(isPlayingStopped){
            mediaPlayer = initNewMediaPlayer(recordingsNames[position])
            preparePlaying(mediaPlayer)
            currentlyPlayedRecording = position
            isPlayingStopped = false
        }

        if(!mediaPlayer!!.isPlaying){
            mediaPlayer?.start()
        } else{
            mediaPlayer?.pause()
        }
    }

    private fun initNewMediaPlayer(name: String): MediaPlayer?{
        var mediaPlayer: MediaPlayer? = MediaPlayer().apply {
            setDataSource("${applicationContext.filesDir.absolutePath}/$name")
        }
        mediaPlayer?.setOnCompletionListener {
            mediaPlayer?.stop()
            isPlayingStopped = true
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
}