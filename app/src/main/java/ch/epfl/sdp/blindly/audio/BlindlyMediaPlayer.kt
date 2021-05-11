package ch.epfl.sdp.blindly.audio

import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Chronometer
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton

private const val PLAYBAR_DELAY = 10L

/**
 * Utility class functions that contains all the functions needed for
 * a mediaPlayer to work
 */
class BlindlyMediaPlayer {
    var mediaPlayer: MediaPlayer? = null
    private var isPlayerPaused = false
    private var isPlayerStopped = true
    private var isPlayBarTouched = false

    /**
     * Set up a MediaPlayer along with all the views of the given argument
     *
     * @param playBar
     * @param playTimer
     * @param remainingTimer
     * @param playPauseButton
     * @param audioRecord
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun setupMediaPlayer(
        playBar: SeekBar,
        playTimer: Chronometer,
        remainingTimer: Chronometer,
        playPauseButton: AppCompatImageButton,
        audioRecord: AudioRecord
    ) {
        val movePlayBarThread = createPlayBarThread(playBar)

        bindSeekBarNavigation(
            playBar, playTimer, remainingTimer, playPauseButton,
            movePlayBarThread, audioRecord
        )

        setCountDownTimer(remainingTimer)

        /*
         * Handles clicks of the play/pause button, according to the current state of the media
         * player.
         */
        playPauseButton.setOnClickListener {
            handlePlayBarClick(
                audioRecord,
                playTimer,
                remainingTimer,
                playPauseButton,
                playBar,
                movePlayBarThread
            )
        }
    }

    private fun createMediaPlayer(filePath: String) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(filePath)
            prepare()
        }
    }

    /**
     * Binds to the seekbar the chronometers, so that a change to the seekbar synced with
     * all the other UI elements
     *
     * @param playBar the seekbar
     * @param playTimer the timer for playing
     * @param remainingTimer the timer for remaining time
     * @param playPauseButton the button to play and pause the audio player
     * @param movePlayBarThread the thread on which the seekbar runs
     * @param audioRecord the audio record
     */
    fun bindSeekBarNavigation(
        playBar: SeekBar, playTimer: Chronometer,
        remainingTimer: Chronometer,
        playPauseButton: AppCompatImageButton,
        movePlayBarThread: Runnable, audioRecord: AudioRecord
    ) {
        playBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                playBar: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                    playTimer.base = SystemClock.elapsedRealtime() - mediaPlayer!!.currentPosition
                    remainingTimer.base = SystemClock.elapsedRealtime() -
                            mediaPlayer!!.currentPosition + mediaPlayer!!.duration.toLong()
                }
            }

            /**
             * When the seek bar is touched, the player is paused. If it was already stopped,
             * the player is reset at the current position.
             *
             * @param playBar the corresponding seekbar
             */
            override fun onStartTrackingTouch(playBar: SeekBar) {
                isPlayBarTouched = true
                handlePlayBarClick(
                    audioRecord,
                    playTimer,
                    remainingTimer,
                    playPauseButton,
                    playBar,
                    movePlayBarThread
                )
            }

            /**
             * When the seek bar is released, the mediaplayer seeks the seek bar's position
             * and the timers are updated.
             *
             * @param playBar the corresponding seekbar
             */
            override fun onStopTrackingTouch(playBar: SeekBar) {
                isPlayBarTouched = false
                updatePlayBar(playBar, movePlayBarThread, playBar.max, playBar.progress)
                setPlayView(playTimer, remainingTimer, playBar, movePlayBarThread, playPauseButton)
                mediaPlayer?.start()
            }
        })
    }

    /**
     * Resets the view and media player of a specific record in the list.
     *
     * @param audioRecord the audioRecord
     * @param playTimer the timer for playing
     * @param remainingTimer the timer for remaining time
     * @param playPauseButton the button to play and pause the audio player
     * @param playBar the moving seek bar for the audio file
     */
    fun resetRecordPlayer(
        audioRecord: AudioRecord, playTimer: Chronometer,
        remainingTimer: Chronometer,
        playPauseButton: AppCompatImageButton, playBar: SeekBar
    ) {
        createMediaPlayer(audioRecord.filePath)
        mediaPlayer?.setOnCompletionListener {
            mediaPlayer?.stop()
            setStoppedView(playTimer, remainingTimer, playPauseButton, false)
        }
        // Reset the timers and the play bar
        remainingTimer.base = SystemClock.elapsedRealtime() + mediaPlayer!!.duration.toLong()
        playTimer.base = SystemClock.elapsedRealtime()
        playBar.progress = 0
    }

    /**
     * Creates a thread that handles the playbar updates, according to the progress of the media
     * player.
     *
     * @param playBar the seekbar
     * @return the created thread
     */
    fun createPlayBarThread(playBar: SeekBar): Runnable {
        return object : Runnable {
            override fun run() {
                if (mediaPlayer?.isPlaying == true) {
                    playBar.max = mediaPlayer!!.duration
                    playBar.progress = mediaPlayer!!.currentPosition
                    Handler(Looper.getMainLooper()).postDelayed(this, PLAYBAR_DELAY)
                }
            }
        }
    }

    /**
     * Updates the playbar on a given thread, according to the media player's position and duration.
     *
     * @param playBar the seekbar
     * @param thread the thread on which the seekbar runs
     * @param duration the duration of the audio file
     * @param position the current position in the file
     */
    private fun updatePlayBar(playBar: SeekBar, thread: Runnable, duration: Int, position: Int) {
        playBar.max = duration
        playBar.progress = position

        val handler = Handler(Looper.getMainLooper())
        handler.removeCallbacks(thread)
        handler.postDelayed(thread, PLAYBAR_DELAY)
    }

    /**
     * Set the given timer to a CountDownTimer
     * Setting a timer to count down requires Android N
     *
     * @param timer the timer
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun setCountDownTimer(timer: Chronometer) {
        timer.isCountDown = true
        timer.format = "-%s"
    }

    /**
     * Set the view of all the given UI elements to reflect a stopped mediaPlayer
     *
     * @param playTimer the timer for playing
     * @param remainingTimer the timer for the remaining time
     * @param playPauseButton the button to play or pause the audio
     * @param isPause a boolean
     */
    fun setStoppedView(
        playTimer: Chronometer, remainingTimer: Chronometer,
        playPauseButton: AppCompatImageButton, isPause: Boolean
    ) {
        playTimer.stop()
        remainingTimer.stop()
        if (isPause) {
            isPlayerPaused = true
        } else {
            isPlayerStopped = true
        }
        playPauseButton.setImageResource(android.R.drawable.ic_media_play)
    }

    private fun setPlayView(
        playTimer: Chronometer, remainingTimer: Chronometer,
        playBar: SeekBar, movePlayBarThread: Runnable,
        playPauseButton: AppCompatImageButton
    ) {
        playTimer.start()
        remainingTimer.start()
        isPlayerPaused = false
        isPlayerStopped = false
        updatePlayBar(
            playBar,
            movePlayBarThread,
            mediaPlayer!!.duration,
            mediaPlayer!!.currentPosition
        )
        playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
    }

    /**
     * Handles clicks and drags on the seekbar
     *
     * @param audioRecord the audio record
     * @param playTimer the timer for playing
     * @param remainingTimer the timer for the remaining time
     * @param playPauseButton the button to play or pause the audio
     * @param playBar the seekbar
     * @param movePlayBarThread the thread on which the seekbar runs
     */
    fun handlePlayBarClick(
        audioRecord: AudioRecord,
        playTimer: Chronometer,
        remainingTimer: Chronometer,
        playPauseButton: AppCompatImageButton,
        playBar: SeekBar,
        movePlayBarThread: Runnable
    ) {
        if (isPlayerStopped) {
            resetRecordPlayer(audioRecord, playTimer, remainingTimer, playPauseButton, playBar)
        }

        if (!mediaPlayer!!.isPlaying) {
            mediaPlayer?.start()

            if (!isPlayerPaused) {
                // Reset the play timer
                playTimer.base = SystemClock.elapsedRealtime()
            }
            setPlayView(playTimer, remainingTimer, playBar, movePlayBarThread, playPauseButton)
        } else {
            mediaPlayer?.pause()
            setStoppedView(playTimer, remainingTimer, playPauseButton, true)
        }
    }
}