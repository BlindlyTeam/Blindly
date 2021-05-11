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
import javax.inject.Inject

private const val PROFILE_ID = "profileID"

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

        val profileNameAge = findViewById<TextView>(R.id.profileNameAgeText)
        val profileLocation = findViewById<TextView>(R.id.profileLocationText)
        val profileOrientations = findViewById<TextView>(R.id.profileOrientationsText)
        val profilePassions = findViewById<TextView>(R.id.profilePassionsText)

        viewModel.user.observe(this) {
            val age = User.getAgeFromBirthday(it.birthday!!)
            profileNameAge.text = "${it.username}, $age"
            profileLocation.text = AndroidLocationService.getCurrentLocationStringFromUser(this, it)
            profileOrientations.text = it.sexualOrientations!!.joinToString(", ")
            profilePassions.text = "Likes ${it.passions!!.joinToString(", \n")}"
        }
    }

    private fun instantiateViewModel(uid: String) {
        val bundle = Bundle()
        bundle.putString(UserHelper.EXTRA_UID, uid)

        val viewModelFactory = assistedFactory.create(this, bundle)

        viewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]
    }
}