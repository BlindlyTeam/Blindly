package ch.epfl.sdp.blindly.main_screen.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.audio.AudioPlayerFragment
import ch.epfl.sdp.blindly.main_screen.profile.edit.EditProfile
import ch.epfl.sdp.blindly.main_screen.profile.settings.Settings
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.viewmodel.UserViewModel
import ch.epfl.sdp.blindly.viewmodel.ViewModelAssistedFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Fragment containing the profile page
 */

const val BUNDLE_UID = "uid"
@AndroidEntryPoint
class ProfilePageFragment : Fragment() {

    @Inject
    lateinit var userHelper: UserHelper

    @Inject
    lateinit var assistedFactory: ViewModelAssistedFactory

    private lateinit var viewModel: UserViewModel
    private var uid: String? = null

    companion object {
        private const val TAG = "ProfilePage"
        const val BOUNCE_DURATION: Long = 100
        private const val ARG_COUNT = "profileArgs"
        private var counter: Int? = null

        /**
         * Create a new instance of ProfilePageFragment
         *
         * @param counter the position of the fragment in the TabLayout
         * @return a ProfilePageFragment
         */
        fun newInstance(counter: Int): ProfilePageFragment {
            val fragment = ProfilePageFragment()
            val args = Bundle()
            args.putInt(ARG_COUNT, counter)
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * Set up the viewModel
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            counter = requireArguments().getInt(ARG_COUNT)
        }

        uid = userHelper.getUserId()
        viewModel = UserViewModel.instantiateViewModel(
            uid,
            assistedFactory,
            this,
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile_page, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userInfoText = view.findViewById<TextView>(R.id.user_info_text)
        val editButton = view.findViewById<Button>(R.id.edit_info_profile_button)
        setOnClickListener(editButton, Intent(context, EditProfile::class.java))

        //Clicking outside of the audioPlayer dismisses it
        val profileView = view.findViewById<RelativeLayout>(R.id.profile_relativeLayout)
        profileView.setOnTouchListener { _, _ ->
            removeAudioPlayer()
            false
        }

        val playAudioButton = view.findViewById<Button>(R.id.play_audio_profile_fragment_button)
        playAudioButton.setOnClickListener {
            showAudioPlayer(playAudioButton)
        }

        val settingsButton = view.findViewById<Button>(R.id.settings_profile_button)
        setOnClickListener(settingsButton, Intent(context, Settings::class.java))

        viewModel.user.observe(viewLifecycleOwner) {
            userInfoText.text = getString(
                R.string.user_info, it.username,
                User.getUserAge(it)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.userUpdate()
    }

    /**
     * An onClickListener that start an Activity after the button has stopped bouncing
     *
     * @param button the button associated with the click
     * @param intent the intent containing the next Activity to start
     */
    private fun setOnClickListener(button: Button, intent: Intent) {
        val bounce = AnimationUtils.loadAnimation(context, R.anim.bouncy_button)
        button.setOnClickListener {
            button.startAnimation(bounce)
            Handler(Looper.getMainLooper()).postDelayed({
                removeAudioPlayer()
                startActivity(intent)
            }, BOUNCE_DURATION)
        }
    }

    private fun showAudioPlayer(button: Button) {
        val bounce = AnimationUtils.loadAnimation(context, R.anim.bouncy_button)
        button.startAnimation(bounce)
        Handler(Looper.getMainLooper()).postDelayed({
            childFragmentManager.commit {
                setReorderingAllowed(true)
                val bundle = Bundle()
                bundle.putString(BUNDLE_UID, uid)
                add(R.id.fragment_audio_container_view, AudioPlayerFragment::class.java, bundle)
            }
        }, BOUNCE_DURATION)
    }

    private fun removeAudioPlayer() {
        childFragmentManager.commit {
            val fragment =
                childFragmentManager.findFragmentById(R.id.fragment_audio_container_view)
            if (fragment != null) {
                val audioPlayerFragment = fragment as AudioPlayerFragment
                remove(audioPlayerFragment)
            }
        }
    }
}