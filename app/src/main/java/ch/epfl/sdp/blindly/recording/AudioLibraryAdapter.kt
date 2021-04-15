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
import ch.epfl.sdp.blindly.profile.ProfileFinished
import java.io.File

private const val PRESENTATION_AUDIO_NAME = "PresentationAudio.3gp"
private const val PLAYBAR_DELAY = 10L

/**
 * Adapter to use a RecyclerView as an audio library.
 *
 * @property recordList list of AudioRecords currently in the adapter
 * @property viewHolderList list of ViewHolders corresponding to each record
 * @property context context of the RecyclerView
 * @property listener handles clicks on items
 */
class AudioLibraryAdapter(var recordList: ArrayList<AudioRecord>,
                          var viewHolderList: ArrayList<ViewHolder>,
                          var context: Context,
                          private val listener: OnItemClickListener)
    : RecyclerView.Adapter<AudioLibraryAdapter.ViewHolder>() {
    var mediaPlayer: MediaPlayer? = null
    private var isPlayerPaused = false
    private var isPlayerStopped = true
    private var isPlayBarTouched = false

    /**
     * Custom ViewHolder class that contains all the elements that will be used later on in
     * [onBindViewHolder].
     *
     * @constructor
     * Sets the click listener for the layout that handles the layout expand click (the one with
     * the name and duration), and sets the visibility of the expandable layout to gone.
     *
     * @param view the current view
     */
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
            nameDurationLayout.setOnClickListener(this)
            expandableLayout.visibility = View.GONE
        }

        override fun onClick(v: View?) {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    fun collapseLayouts() {
        for (i in 0 until recordList.size) {
            if (recordList[i].isExpanded) {
                val viewHolder = viewHolderList[i]
                setStoppedView(viewHolder.playTimer, viewHolder.remainingTimer,
                        viewHolder.playPauseButton, false)
                RecordAnimations.collapse(viewHolder.expandableLayout)
                recordList[i].isExpanded = false
            }
        }
    }

    /**
     * Expands or collapse the layout of the audio file, and collapse other ones.
     *
     * @param isExpanded if the record is currently expanded in layout
     * @param view the current view
     * @param layoutExpand the layout to expand/collapse
     */
    private fun toggleLayout(isExpanded: Boolean, view: View, layoutExpand: RelativeLayout) {
        collapseLayouts()
        if (isExpanded) {
            RecordAnimations.expand(layoutExpand)
        } else {
            RecordAnimations.collapse(layoutExpand)
        }
    }

    /**
     * Create new views for each item of the list.
     * This function is invoked by the layout manager.
     *
     * @param viewGroup the group where the ViewHolder belongs
     * @param viewType the type of view
     * @return a new view, which defines the UI of the list items
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.audio_recording, null)
        viewHolderList.add(ViewHolder(view))
        return ViewHolder(view)
    }

    /**
     * Binds all the content to the view, and handles click listeners.
     * This function is invoked by the layout manager.
     *
     * @param viewHolder the ViewHolder of the current item
     * @param position the position of the item in the RecyclerView
     */
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val playBar = viewHolder.playBar
        val playTimer = viewHolder.playTimer
        val remainingTimer = viewHolder.remainingTimer
        val playPauseButton = viewHolder.playPauseButton
        val movePlayBarThread = createPlayBarThread(playBar)

        bindSeekBarNavigation(playBar, playTimer, remainingTimer, playPauseButton,
                movePlayBarThread, position)

        viewHolder.recordName.text = recordList[position].name
        viewHolder.recordDuration.text = recordList[position].durationText
        viewHolder.nameDurationLayout.setOnClickListener {
            val notIsExpanded = !recordList[position].isExpanded
            toggleLayout(notIsExpanded, it, viewHolder.expandableLayout)
            recordList[position].isExpanded = notIsExpanded
            resetRecordPlayer(position, playTimer, remainingTimer, playPauseButton, playBar)
        }

        setCountDownTimer(remainingTimer)

        viewHolder.playPauseButton.setOnClickListener {
            if (isPlayerStopped) {
                resetRecordPlayer(position, playTimer, remainingTimer, playPauseButton, playBar)
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

    private fun resetRecordPlayer(position: Int, playTimer: Chronometer,
                                  remainingTimer: Chronometer,
                                  playPauseButton: AppCompatImageButton, playBar: SeekBar) {
        createMediaPlayer(recordList[position].filePath)
        mediaPlayer?.setOnCompletionListener {
            mediaPlayer?.stop()
            setStoppedView(playTimer, remainingTimer, playPauseButton, false)
        }
        remainingTimer.base = SystemClock.elapsedRealtime() + mediaPlayer!!.duration.toLong()
        playTimer.base = SystemClock.elapsedRealtime()
        playBar.progress = 0
    }

    private fun saveRecording(position: Int) {
        val filePath = recordList[position].filePath
        val newName = PRESENTATION_AUDIO_NAME
        val currentRecording = File(filePath)
        currentRecording.copyTo(File("${context.filesDir.absolutePath}/$newName"),
                overwrite = true)
    }

    private fun createMediaPlayer(filePath: String) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(filePath)
            prepare()
        }
    }

    private fun bindSeekBarNavigation(playBar: SeekBar, playTimer: Chronometer,
                                      remainingTimer: Chronometer,
                                      playPauseButton: AppCompatImageButton,
                                      playBarThread: Runnable, position: Int) {
        playBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(playBar: SeekBar,
                                           progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(playBar: SeekBar) {
                isPlayBarTouched = true
                if (isPlayerStopped) {
                    resetRecordPlayer(position, playTimer, remainingTimer, playPauseButton, playBar)
                } else {
                    mediaPlayer?.pause()
                    setStoppedView(playTimer, remainingTimer, playPauseButton, true)
                }
            }

            override fun onStopTrackingTouch(playBar: SeekBar) {
                isPlayBarTouched = false
                updatePlayBar(playBar, playBarThread, playBar.max, playBar.progress)
                mediaPlayer?.seekTo(playBar.progress)
                playTimer.base = SystemClock.elapsedRealtime() - mediaPlayer!!.currentPosition
                remainingTimer.base = SystemClock.elapsedRealtime() -
                        mediaPlayer!!.currentPosition + mediaPlayer!!.duration.toLong()
                setPlayView(playTimer, remainingTimer, playBar, playBarThread, playPauseButton)
                mediaPlayer?.start()
            }
        })
    }

    private fun createPlayBarThread(playBar: SeekBar): Runnable {
        return object : Runnable {
            override fun run() {
                if (mediaPlayer?.isPlaying == true && !isPlayBarTouched) {
                    playBar.max = mediaPlayer!!.duration
                    playBar.progress = mediaPlayer!!.currentPosition
                    Handler(Looper.getMainLooper()).postDelayed(this, PLAYBAR_DELAY)
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
        handler.removeCallbacks(thread);
        handler.postDelayed(thread, PLAYBAR_DELAY);
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

    override fun getItemCount() = recordList.size

    /**
     * Handles clicks on different items
     */
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}