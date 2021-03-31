package ch.epfl.sdp.blindly.recording

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.animations.RecordAnimations

/**
 * Serves as an adapter to add audio recordings in a RecyclerView
 */
class AudioLibraryAdapter(var recordList: ArrayList<AudioRecord>,
                          var context: Context,
                          private val listener: OnItemClickListener)
    : RecyclerView.Adapter<AudioLibraryAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    var currentSelectionPos = -1
    private var mediaPlayer: MediaPlayer? = null
    private var isPlayerPaused = false

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val recordName: TextView = view.findViewById(R.id.recordName)
        val recordDuration: TextView = view.findViewById(R.id.recordDuration)
        val nameDurationLayout: LinearLayout = view.findViewById(R.id.nameDurationLayout)
        val expandableLayout: RelativeLayout = view.findViewById(R.id.audioPlayLayout)
        val playPauseButton: AppCompatImageButton = view.findViewById(R.id.playPauseButton)
        val playBar: SeekBar = view.findViewById(R.id.playBar)
        val playTimer: Chronometer = view.findViewById(R.id.audioTimer)
        val remainingTimer: Chronometer = view.findViewById(R.id.remainingTimer)

        init {
            // Define click listener for the ViewHolder's View.
            nameDurationLayout.setOnClickListener(this)
            expandableLayout.visibility = View.GONE
        }

        override fun onClick(v: View?) {
            // Forward the click to the main activity
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    // Expands or collapse the layout for playing the audio file
    private fun toggleLayout(isExpanded: Boolean, v: View, layoutExpand: RelativeLayout): Boolean {
        if (isExpanded) {
            RecordAnimations.expand(layoutExpand)
        } else {
            RecordAnimations.collapse(layoutExpand)
        }
        return isExpanded
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(context).inflate(R.layout.audio_recording, null)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.recordName.text = recordList[position].name
        viewHolder.recordDuration.text = recordList[position].durationText

        viewHolder.nameDurationLayout.setOnClickListener {
            val show: Boolean = toggleLayout(!recordList[position].isExpanded, it, viewHolder.expandableLayout)
            recordList[position].isExpanded = show
        }

        val playBar = viewHolder.playBar
        val playTimer = viewHolder.playTimer
        val remainingTimer = viewHolder.remainingTimer
        val playPauseButton = viewHolder.playPauseButton
        val movePlayBarThread = createPlayBarThread(playBar)

        remainingTimer.format = "-%s"

        viewHolder.playPauseButton.setOnClickListener {
            createMediaPlayer(recordList[position].filePath, playTimer)
            if (mediaPlayer!!.isPlaying) {
                playTimer.stop()
                remainingTimer.stop()
                mediaPlayer!!.pause()
                isPlayerPaused = true
                // Changing images doesn't seem to work
                playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
            } else {
                if (!isPlayerPaused) {
                    playTimer.base = SystemClock.elapsedRealtime()
                }
                playTimer.start()
                remainingTimer.start()
                mediaPlayer!!.start()
                updatePlayBar(playBar, movePlayBarThread, mediaPlayer!!.duration, mediaPlayer!!.currentPosition)
                isPlayerPaused = false
                // Changing images doesn't seem to work
                playPauseButton.setImageResource(android.R.drawable.ic_media_play)
            }
        }
    }

    private fun createMediaPlayer(filePath: String, timer: Chronometer) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(filePath)
            prepare()
        }
        mediaPlayer?.setOnCompletionListener {
            mediaPlayer?.stop()
            timer.stop()
            timer.base = SystemClock.elapsedRealtime()
        }
    }

    private fun createPlayBarThread(playBar: SeekBar): Runnable {
        return object : Runnable {
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

    private fun updatePlayBar(playBar: SeekBar, thread: Runnable, duration: Int, position: Int) {
        playBar.max = duration
        playBar.progress = position

        val handler = Handler(Looper.getMainLooper())
        handler.removeCallbacks(thread);
        handler.postDelayed(thread, 100);
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = recordList.size

    /**
     * Used to handle clicks in the activity
     */
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}