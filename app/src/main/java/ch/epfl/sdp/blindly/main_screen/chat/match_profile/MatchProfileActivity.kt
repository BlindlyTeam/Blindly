package ch.epfl.sdp.blindly.main_screen.chat.match_profile

import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.database.UserRepository
import ch.epfl.sdp.blindly.location.AndroidLocationService
import ch.epfl.sdp.blindly.user.User
import ch.epfl.sdp.blindly.user.UserHelper
import ch.epfl.sdp.blindly.viewmodel.UserViewModel
import ch.epfl.sdp.blindly.viewmodel.ViewModelAssistedFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

const val PROFILE_ID = "profileID"

/**
 * Activity that shows more details about a potential match
 */
@AndroidEntryPoint
class MatchProfileActivity : AppCompatActivity() {
    private lateinit var viewModel: UserViewModel
    private lateinit var audioFilePath: String

    @Inject
    lateinit var assistedFactory: ViewModelAssistedFactory

    @Inject
    lateinit var storage: FirebaseStorage

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.match_profile)

        // Cancels loading if the profileID isn't given in the Bundle
        val profileID = intent.extras?.getString(PROFILE_ID) ?: return
        instantiateViewModel(profileID)

        supportActionBar?.hide()

        val profileNameAge = findViewById<TextView>(R.id.matchProfileNameAge)
        val profileGender = findViewById<TextView>(R.id.matchProfileGender)
        val profileLocation = findViewById<TextView>(R.id.matchProfileLocation)
        val profileOrientations = findViewById<ChipGroup>(R.id.matchProfileOrientations)
        val profilePassions = findViewById<ChipGroup>(R.id.matchProfilePassions)

        bindPlayButton(findViewById(R.id.matchProfilePlayAudioButton))

        viewModel.user.observe(this) { user ->
            val age = User.getAgeFromBirthday(user.birthday!!)
            profileNameAge.text = "${user.username}, $age"
            profileGender.text = user.gender
            profileLocation.text = AndroidLocationService.getCurrentLocationStringFromUser(this, user)
            user.sexualOrientations?.let { it -> setCheckedChips(profileOrientations, it) }
            user.passions?.let { it -> setCheckedChips(profilePassions, it) }
            audioFilePath = user.recordingPath!!
        }
    }

    private fun bindPlayButton(button: Button) {
        val bounce = AnimationUtils.loadAnimation(this, R.anim.bouncy_button)
        button.setOnClickListener {
            button.startAnimation(bounce)
            playAudio(it)
        }
    }

    private fun instantiateViewModel(uid: String) {
        val bundle = Bundle()
        bundle.putString(UserHelper.EXTRA_UID, uid)

        val viewModelFactory = assistedFactory.create(this, bundle)

        viewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]
    }

    private fun setCheckedChips(chipGroup: ChipGroup, text: List<String>) {
        for (t in text) {
            val chip = Chip(this)
            chip.text = t
            chipGroup.addView(chip)
        }
    }

    private fun playAudio(view: View) {
        // Create a storage reference from our app
        val storageRef = storage.reference
        // Create a reference with the recordingPath
        val pathRef = storageRef.child(audioFilePath)
        val audioFile = File.createTempFile("Audio", "amr")
        pathRef.getFile(audioFile).addOnSuccessListener {
            val mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(this, Uri.fromFile(audioFile))
            mediaPlayer.prepare()
            mediaPlayer.start()
        }
    }
}