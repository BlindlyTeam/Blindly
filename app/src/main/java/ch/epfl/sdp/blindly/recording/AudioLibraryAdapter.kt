package ch.epfl.sdp.blindly.recording

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.animations.RecordAnimations
import ch.epfl.sdp.blindly.profile_setup.ProfileFinished
import java.io.File

private const val PRESENTATION_AUDIO_NAME = "PresentationAudio.3gp"

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
    var mediaPlayer: MediaPlayer? = null
    private var isPlayerPaused = false
    private var isPlayerStopped = true

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val recordName: TextView = view.findViewById(R.id.recordName)
        val recordDuration: TextView = view.findViewById(R.id.recordDuration)
        val nameDurationLayout: LinearLayout = view.findViewById(R.id.nameDurationLayout)
        val expandableLayout: RelativeLayout = view.findViewById(R.id.audioPlayLayout)
        val playPauseButton: AppCompatImageButton = view.findViewById(R.id.playPauseButton)
        val playBar: SeekBar = view.findViewById(R.id.playBar)
        val playTimer: Chronometer = view.findViewById(R.id.audioTimer)
        val remainingTimer: Chronometer = view.findViewById(R.id.remainingTimer)
        val selectButton: Button = view.findViewById(R.id.selectButton)

        init {
            // Define click listener for the ViewHolder's View.
            nameDurationLayout.setOnClickListener(this)
            expandableLayout.visibility = View.GONE
        }

        override fun onClick(v: View?) {
            // Forward the click to the main activity
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    // Expands or collapse the layout for playing the audio file
    private fun toggleLayout(isExpanded: Boolean, v: View, layoutExpand: RelativeLayout) {
        if (isExpanded) {
            RecordAnimations.expand(layoutExpand)
        } else {
            RecordAnimations.collapse(layoutExpand)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(context).inflate(R.layout.audio_recording, null)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.recordName.text = recordList[position].name
        viewHolder.recordDuration.text = recordList[position].durationText
        viewHolder.nameDurationLayout.setOnClickListener {
            val notIsExpanded = !recordList[position].isExpanded
            toggleLayout(notIsExpanded, it, viewHolder.expandableLayout)
            recordList[position].isExpanded = notIsExpanded
        }

        val playBar = viewHolder.playBar
        val playTimer = viewHolder.playTimer
        val remainingTimer = viewHolder.remainingTimer
        val playPauseButton = viewHolder.playPauseButton
        val movePlayBarThread = createPlayBarThread(playBar)

        setCountDownTimer(remainingTimer)

        viewHolder.playPauseButton.setOnClickListener {
            if (isPlayerStopped) {
                createMediaPlayer(recordList[position].filePath)
                remainingTimer.base = SystemClock.elapsedRealtime() + mediaPlayer!!.duration.toLong()

                mediaPlayer?.setOnCompletionListener {
                    mediaPlayer?.stop()
                    setStoppedView(playTimer, remainingTimer, playPauseButton, false)
                }
            }
            if (!mediaPlayer!!.isPlaying) {
                mediaPlayer?.start()

                if (!isPlayerPaused) {
                    playTimer.base = SystemClock.elapsedRealtime()
                }
                setPlayView(playTimer, remainingTimer, playBar, movePlayBarThread, playPauseButton)
            } else {
                mediaPlayer?.pause()
                setStoppedView(playTimer, remainingTimer, playPauseButton, true)
            }
        }

        viewHolder.selectButton.setOnClickListener {
            mediaPlayer?.release()
            saveRecording(position)
            startProfileFinished()
        }
    }

    private fun saveRecording(position: Int) {
        val filePath = recordList[position].filePath
        val newName = PRESENTATION_AUDIO_NAME
        val currentRecording = File(filePath)
        currentRecording.copyTo(File("${context.filesDir.absolutePath}/$newName"), overwrite = true)
    }

    private fun createMediaPlayer(filePath: String) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(filePath)
            prepare()
        }
    }

    private fun createPlayBarThread(playBar: SeekBar): Runnable {
        return object : Runnable {
            override fun run() {
                if (mediaPlayer?.isPlaying == true) {
                    playBar.max = mediaPlayer!!.duration
                    playBar.progress = mediaPlayer!!.currentPosition
                    Handler(Looper.getMainLooper()).postDelayed(this, 100)
                }
            }
        }
    }

    private fun startProfileFinished() {
        val intent = Intent(context, ProfileFinished::class.java)
        startActivity(context, intent, null)
    }

    private fun updatePlayBar(playBar: SeekBar, thread: Runnable, duration: Int, position: Int) {
        playBar.max = duration
        playBar.progress = position

        val handler = Handler(Looper.getMainLooper())
        handler.removeCallbacks(thread)
        handler.postDelayed(thread, 100)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setCountDownTimer(timer: Chronometer) {
        timer.isCountDown = true
        timer.format = "-%s"
    }

    private fun setStoppedView(playTimer: Chronometer, remainingTimer: Chronometer,
                               playPauseButton: AppCompatImageButton, isPause: Boolean) {
        playTimer.stop()
        remainingTimer.stop()
        if (isPause) {
            isPlayerPaused = true
        } else {
            isPlayerStopped = true
        }
        playPauseButton.setImageResource(android.R.drawable.ic_media_play)
    }

    private fun setPlayView(playTimer: Chronometer, remainingTimer: Chronometer,
                            playBar: SeekBar, movePlayBarThread: Runnable,
                            playPauseButton: AppCompatImageButton) {
        playTimer.start()
        remainingTimer.start()
        isPlayerPaused = false
        isPlayerStopped = false
        updatePlayBar(playBar, movePlayBarThread, mediaPlayer!!.duration, mediaPlayer!!.currentPosition)
        playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
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