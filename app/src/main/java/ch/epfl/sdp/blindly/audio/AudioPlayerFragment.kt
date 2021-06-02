package ch.epfl.sdp.blindly.audio

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.main_screen.profile.BUNDLE_UID
import ch.epfl.sdp.blindly.viewmodel.UserViewModel
import ch.epfl.sdp.blindly.viewmodel.ViewModelAssistedFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [AudioPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val MY_AUDIO_RECORD = "My Audio Record"

@AndroidEntryPoint
class AudioPlayerFragment : Fragment() {

    @Inject
    lateinit var assistedFactory: ViewModelAssistedFactory

    private val blindlyMediaPlayer = BlindlyMediaPlayer()
    private lateinit var playBar: SeekBar
    private lateinit var remainingTimer: Chronometer
    private lateinit var playTimer: Chronometer
    private lateinit var playPauseButton: FloatingActionButton
    private var uid: String? = null
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uid = it.getString(BUNDLE_UID)
        }

        viewModel = UserViewModel.instantiateViewModel(
            uid,
            assistedFactory,
            this,
            this
        )
    }


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

        playBar = view.findViewById(R.id.play_bar)
        remainingTimer = view.findViewById(R.id.remaining_timer)
        playTimer = view.findViewById(R.id.audio_timer)
        playPauseButton = view.findViewById(R.id.play_pause_button)

        initializeMediaPlayer()

        val recordName = view.findViewById<TextView>(R.id.record_name)
        recordName.text = MY_AUDIO_RECORD

        val recordButton = view.findViewById<Button>(R.id.record_button)
        recordButton.setOnClickListener {
            val intent = Intent(context, RecordingActivity::class.java)
            startActivity(intent)
            removeFragment()
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initializeMediaPlayer() {
        this.context?.let {
            viewModel.prepareUserMediaPlayer(
                blindlyMediaPlayer,
                playBar,
                remainingTimer,
                playTimer,
                playPauseButton,
                it
            )
        }
    }


    private fun removeFragment() {
        viewModel.resetUserMediaPlayer(
            blindlyMediaPlayer,
            playBar,
            remainingTimer,
            playTimer,
            playPauseButton
        )
        blindlyMediaPlayer.mediaPlayer?.release()
        parentFragmentManager
            .beginTransaction()
            .remove(this)
            .commitAllowingStateLoss()
    }

    companion object {
        private const val TAG = "AudioPlayer"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param uid the uid of the current user
         * @return A new instance of fragment AudioPlayerFragment.
         */
        @JvmStatic
        fun newInstance(uid: String) =
            AudioPlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(BUNDLE_UID, uid)
                }
            }
    }
}