package ch.epfl.sdp.blindly.match

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import kotlinx.coroutines.*
import javax.inject.Inject

private const val PROFILE_ID = "profileID"
private const val NO_PASSIONS = "No passions provided"
private const val NO_ORIENTATIONS = "No sexual orientations provided"

/**
 * Activity that shows more details about a potential match
 */
class MatchProfileActivity : AppCompatActivity() {
    private lateinit var viewModel: UserViewModel

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var userHelper: UserHelper

    @Inject
    lateinit var assistedFactory: ViewModelAssistedFactory

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.match_profile)

        // Cancels loading if the profileID isn't given in the Bundle
        val profileID = intent.extras?.getString(PROFILE_ID) ?: return
        instantiateViewModel(profileID)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUserView(user: User) {
        val profileNameAge = findViewById<TextView>(R.id.profileNameAgeText)
        val profileLocation = findViewById<TextView>(R.id.profileLocationText)
        val profileDescription = findViewById<TextView>(R.id.profileDescriptionText)
        val profileOrientations = findViewById<TextView>(R.id.profileOrientationsText)
        val profilePassions = findViewById<TextView>(R.id.profilePassionsText)

        val age = user.birthday?.let { User.getAgeFromBirthday(it) }
        val locationText = AndroidLocationService.getCurrentLocationStringFromUser(this,
            user)
        profileNameAge.text = "${user.username}, $age"
        profileLocation.text = locationText
        profileDescription.text = user.description
        profileOrientations.text = user.sexualOrientations?.joinToString(", ")
            ?: NO_ORIENTATIONS
        profilePassions.text = user.passions?.joinToString(", ") ?: NO_PASSIONS
    }

    private fun instantiateViewModel(uid: String) {
        val bundle = Bundle()
        bundle.putString(UserHelper.EXTRA_UID, uid)

        val viewModelFactory = assistedFactory.create(this, bundle)

        viewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]
    }
}