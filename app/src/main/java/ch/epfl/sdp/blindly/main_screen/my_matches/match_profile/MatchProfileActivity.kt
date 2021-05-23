package ch.epfl.sdp.blindly.main_screen.my_matches.match_profile

import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.location.AndroidLocationService.Companion.getCurrentLocationStringFromUser
import ch.epfl.sdp.blindly.main_screen.my_matches.MyMatchesAdapter.Companion.BUNDLE_MATCHED_UID_LABEL
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.storage.UserCache
import ch.epfl.sdp.blindly.viewmodel.UserViewModel
import ch.epfl.sdp.blindly.viewmodel.UserViewModel.Companion.instantiateViewModel
import ch.epfl.sdp.blindly.viewmodel.ViewModelAssistedFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

/**
 * Activity that shows more details about a potential match
 */
@AndroidEntryPoint
class MatchProfileActivity : AppCompatActivity() {
    private var profileID: String? = null
    private lateinit var viewModel: UserViewModel
    private var audioFilePath: String? = null
    private var mediaPlayer: MediaPlayer? = null

    @Inject
    lateinit var assistedFactory: ViewModelAssistedFactory

    @Inject
    lateinit var storage: FirebaseStorage

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var userCache: UserCache

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_profile)

        // Cancels loading if the profileID isn't given in the Bundle
        if (intent.extras?.containsKey(BUNDLE_MATCHED_UID_LABEL) == true) {
            profileID = intent.extras!!.getString(BUNDLE_MATCHED_UID_LABEL)

            viewModel = instantiateViewModel(
                profileID, assistedFactory,
                this, this
            )
        } else {
            return
        }

        supportActionBar?.hide()

        val profileNameAge = findViewById<TextView>(R.id.matchProfileNameAge)
        val profileGender = findViewById<TextView>(R.id.matchProfileGender)
        val profileLocation = findViewById<TextView>(R.id.matchProfileLocation)
        val profileOrientations = findViewById<ChipGroup>(R.id.matchProfileOrientations)
        val profilePassions = findViewById<ChipGroup>(R.id.matchProfilePassions)

        bindPlayButton(findViewById(R.id.matchProfilePlayAudioButton))

        viewModel.user.observe(this) { user ->
            profileNameAge.text = getString(
                R.string.user_info, user.username,
                User.getUserAge(user)
            )
            profileGender.text = user.gender
            profileLocation.text = getCurrentLocationStringFromUser(this, user)
            user.sexualOrientations?.let { it -> setCheckedChips(profileOrientations, it) }
            user.passions?.let { it -> setCheckedChips(profilePassions, it) }
            audioFilePath = user.recordingPath
        }

        // The condition can be removed once we're sure that every user has an audio file path
        // attached to it
        if (audioFilePath != null)
            prepareMediaPlayer()
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }

    private fun bindPlayButton(button: Button) {
        val bounce = AnimationUtils.loadAnimation(this, R.anim.bouncy_button)
        button.setOnClickListener {
            button.startAnimation(bounce)
            mediaPlayer?.start()
        }
    }

    private fun setCheckedChips(chipGroup: ChipGroup, text: List<String>) {
        for (t in text) {
            val chip = Chip(this)
            chip.text = t
            chipGroup.addView(chip)
        }
    }

    private fun prepareMediaPlayer() {
        // Create a storage reference from our app
        val storageRef = storage.reference
        // Create a reference with the recordingPath
        val pathRef = storageRef.child(audioFilePath!!)
        val audioFile = File.createTempFile("MatchProfile_Audio", "amr")
        pathRef.getFile(audioFile).addOnSuccessListener {
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setDataSource(this, Uri.fromFile(audioFile))
            mediaPlayer!!.setOnCompletionListener {
                it.stop()
            }
            mediaPlayer!!.prepare()
        }
    }
}