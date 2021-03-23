package ch.epfl.sdp.blindly

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AudioLibrary : AppCompatActivity(), RecordingAdapter.OnItemClickListener {
    private lateinit var recordingList: RecyclerView
    private lateinit var adapterTest: RecordingAdapter
    private val dataSet = arrayOf("Cow", "Horse", "Camel", "Goat")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_library)

        supportActionBar?.hide()

        recordingList = findViewById(R.id.recordingList)
        recordingList.layoutManager = LinearLayoutManager(this)
        adapterTest = RecordingAdapter(dataSet, this)
        recordingList.adapter = adapterTest
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this, "You clicked item $position", Toast.LENGTH_SHORT).show()
        dataSet[position] = "NEW"
        adapterTest.notifyItemChanged(position)
    }
}