package ch.epfl.sdp.blindly.main_screen.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ch.epfl.sdp.blindly.EditProfile
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.ViewModelAssistedFactory
import ch.epfl.sdp.blindly.ViewModelFactory
import ch.epfl.sdp.blindly.recording.RecordingActivity
import ch.epfl.sdp.blindly.settings.Settings
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.UserHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val BOUNCE_DURATION: Long = 100

@AndroidEntryPoint
class ProfilePage : Fragment() {

    @Inject
    lateinit var userHelper: UserHelper
    @Inject
    lateinit var assistedFactory: ViewModelAssistedFactory

    private lateinit var viewModel: ProfilePageViewModel

    companion object {
        private const val TAG = "ProfilePage"
        private const val ARG_COUNT = "profileArgs"
        private var counter: Int? = null;

        fun newInstance(counter: Int): ProfilePage {
            val fragment = ProfilePage();
            val args = Bundle();
            args.putInt(ARG_COUNT, counter);
            fragment.arguments = args;
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

        val bundle = Bundle()
        bundle.putString("uid", userHelper.getUserId())

        val viewModelFactory = assistedFactory.create(this, bundle)

        viewModel = ViewModelProvider(this, viewModelFactory)[ProfilePageViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_profile_page, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userInfoText = view.findViewById<TextView>(R.id.user_info_text)
        val userDescriptionText = view.findViewById<TextView>(R.id.user_description_text)

        val context = this@ProfilePage.context

        val editButton = view.findViewById<Button>(R.id.edit_info_profile_button)
        setOnClickListener(editButton, Intent(context, EditProfile::class.java))

        val recordAudioButton = view.findViewById<Button>(R.id.record_audio_profile_button)
        setOnClickListener(recordAudioButton, Intent(context, RecordingActivity::class.java))

        val settingsButton = view.findViewById<Button>(R.id.settings_profile_button)
        setOnClickListener(settingsButton, Intent(context, Settings::class.java))

        viewModel.user.observe(viewLifecycleOwner) {
            userInfoText.text = it.username + ", ${User.getUserAge(it)}"
            if(it.description == "") {
                userDescriptionText.text = "Add description"
            } else {
                userDescriptionText.text = it.description
            }
        }
    }


    /**
     * An onClickListener that start an Activity after the button has stopped bouncing
     * @param button: the button associated with the click
     * @param intent: the intent containing the next Activity to start
     */
    private fun setOnClickListener(button: Button, intent: Intent) {
        val bounce = AnimationUtils.loadAnimation(context, R.anim.bouncy_button)
        button.setOnClickListener {
            button.startAnimation(bounce)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
            }, BOUNCE_DURATION)
        }
    }

}