package ch.epfl.sdp.blindly.match

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import ch.epfl.sdp.blindly.R
import ch.epfl.sdp.blindly.database.UserRepository
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
    private var userShown: User? = null
    private lateinit var viewModel: UserViewModel

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var assistedFactory: ViewModelAssistedFactory

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.match_profile)

        val profileID = intent.extras?.getString(PROFILE_ID)
        val bundle = Bundle()
        bundle.putString(UserHelper.EXTRA_UID, profileID)

        val viewModelFactory = assistedFactory.create(this, bundle)

        viewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]

        val profileNameAge = findViewById<TextView>(R.id.profileNameAgeText)
        val profileLocation = findViewById<TextView>(R.id.profileLocationText)
        val profileDescription = findViewById<TextView>(R.id.profileDescriptionText)
    }
}