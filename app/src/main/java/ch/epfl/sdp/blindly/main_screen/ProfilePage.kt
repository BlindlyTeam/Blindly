package ch.epfl.sdp.blindly.main_screen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ch.epfl.sdp.blindly.AudioLibrary
import ch.epfl.sdp.blindly.EditProfile
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.recording.RecordingActivity
import ch.epfl.sdp.blindly.settings.Settings
import ch.epfl.sdp.blindly.user.UserCache
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.user.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val BOUNCE_DURATION: Long = 100

@AndroidEntryPoint
class ProfilePage : Fragment() {

    @Inject
    lateinit var userHelper: UserHelper
    @Inject
    lateinit var db: FirebaseFirestore
    @Inject
    lateinit var userCache: UserCache

    private lateinit var viewModel: ProfilePageViewModel
    private lateinit var userRepository: UserRepository

    companion object {
        private const val ARG_COUNT = "profileArgs";
        private var counter: Int? = null;

        fun newInstance(counter: Int): ProfilePage {
            val fragment = ProfilePage();
            val args = Bundle();
            args.putInt(ARG_COUNT, counter);
            fragment.arguments = args;
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            counter = requireArguments().getInt(ARG_COUNT)
        }

        userRepository = UserRepository(db, userCache)
        val bundle = Bundle()
        bundle.putString("uid", userHelper.getUserId())

        viewModel = ViewModelProvider(this, SavedStateVMFactory(userRepository, this, bundle))
            .get(ProfilePageViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_profile_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userInfoText = view.findViewById<TextView>(R.id.user_info_text)
        //userInfoText.text = getString(R.string.user_example_name_and_age)

        val userDescriptionText = view.findViewById<TextView>(R.id.user_description_text)
        //userDescriptionText.text = getString(R.string.student)

        val context = this@ProfilePage.context

        val editButton = view.findViewById<Button>(R.id.edit_info_profile_button)
        setOnClickListener(editButton, Intent(context, EditProfile::class.java))

        val recordAudioButton = view.findViewById<Button>(R.id.record_audio_profile_button)
        setOnClickListener(recordAudioButton, Intent(context, RecordingActivity::class.java))

        val settingsButton = view.findViewById<Button>(R.id.settings_profile_button)
        setOnClickListener(settingsButton, Intent(context, Settings::class.java))

        val audioLibraryButton = view.findViewById<Button>(R.id.audio_library_profile_button)
        setOnClickListener(audioLibraryButton, Intent(context, AudioLibrary::class.java))


        viewModel.user.observe(viewLifecycleOwner) {
            userInfoText.text = it.username
            if(it.description == "") {
                userDescriptionText.text = "Add description"
            } else {
                userDescriptionText.text = it.description
            }
        }
    }

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