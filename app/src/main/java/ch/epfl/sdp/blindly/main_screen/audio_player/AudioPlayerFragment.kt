package ch.epfl.sdp.blindly.main_screen.audio_player

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.recording.AudioRecord
import ch.epfl.sdp.blindly.recording.BlindlyMediaPlayer
import ch.epfl.sdp.blindly.recording.PRESENTATION_AUDIO_NAME
import ch.epfl.sdp.blindly.recording.RecordingActivity
import java.io.File

/**
 * A simple [Fragment] subclass.
 * Use the [AudioPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class AudioPlayerFragment : Fragment() {
    private val blindlyMediaPlayer = BlindlyMediaPlayer()
    private lateinit var audioRecord: AudioRecord
    private lateinit var recordName: TextView
    private lateinit var recordDuration: TextView
    private lateinit var playBar: SeekBar
    private lateinit var remainingTimer: Chronometer
    private lateinit var playTimer: Chronometer
    private lateinit var playPauseButton: AppCompatImageButton

    /**
     * Instantiates the mediaPlayer
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the AudioPlayerFragment view
     */
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_audio_player, container, false)

        val file = File("${context?.filesDir?.absolutePath}/$PRESENTATION_AUDIO_NAME")
        //TODO Can we obtain the duration from a File?
        audioRecord = AudioRecord(
            file.name,
            "",
            file.path,
            true
        )

        playBar = view.findViewById(R.id.play_bar)
        remainingTimer = view.findViewById(R.id.remaining_timer)
        playTimer = view.findViewById(R.id.audio_timer)
        playPauseButton = view.findViewById(R.id.play_pause_button)

        blindlyMediaPlayer.setupMediaPlayer(
            playBar,
            playTimer,
            remainingTimer,
            playPauseButton,
            audioRecord
        )

        recordName = view.findViewById(R.id.record_name)
        recordName.text = audioRecord.name

        recordDuration = view.findViewById(R.id.record_duration)
        recordDuration.text = audioRecord.durationText

        //TODO will have to refactor the RecordingActivity, to accept a User as Bundle
        /*
        val recordButton = view.findViewById<Button>(R.id.record_button)
        recordButton.setOnClickListener {
            val intent = Intent(context, RecordingActivity::class.java)
            removeFragment()
            startActivity(intent)
        }
        */

        return view
    }

    private fun removeFragment() {
        blindlyMediaPlayer.resetRecordPlayer(
            audioRecord,
            playTimer,
            remainingTimer,
            playPauseButton,
            playBar
        )
        parentFragmentManager.commit {
            val audioPlayerFragment =
            parentFragmentManager.findFragmentById(R.id.fragment_audio_container_view)
            remove(audioPlayerFragment!!)
        }
    }

    companion object {
        private const val TAG = "AudioPlayer"
        /**
         * Use this factory method to create a new instance of this fragment
         *
         * @return A new instance of fragment AudioPlayerFragment.
         */
        @JvmStatic
        fun newInstance() = AudioPlayerFragment()
    }
}